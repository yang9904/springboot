import torch
import torch.nn as nn
from torch.distributions import MultivariateNormal, Categorical

device = torch.device('cuda')


class CnnActorCriticNetwork(nn.Module):
    def __init__(self, state_dim, action_dim, action_std_init, has_continuous_action_space, K):
        super(CnnActorCriticNetwork, self).__init__()
        self.action_dim = action_dim
        self.action_std = action_std_init
        self.action_var = torch.full((action_dim,), action_std_init * action_std_init).to(device)
        self.has_continuous_action_space = has_continuous_action_space
        self.K = K

        '''
        向量嵌入
        '''
        self.conv = nn.Sequential(
            nn.Conv2d(in_channels=1, out_channels=32, kernel_size=8, stride=4),
            nn.ReLU(),
            nn.Conv2d(32, 64, 4, 2),
            nn.ReLU(),
            nn.Conv2d(64, 64, 3, 1),
            nn.ReLU()
        )

        self.obs_embedding = nn.Sequential(
            nn.Linear(in_features=7 * 7 * 64, out_features=512),
            nn.ReLU()
        )

        self.state_embedding = nn.Sequential(
            nn.Linear(in_features=state_dim, out_features=128),
            nn.ReLU()
        )

        '''
        RNN层
        '''
        self.gru = nn.GRU(input_size=512, hidden_size=self.hidden_size, batch_first=true)

        '''
        MLP注意力层
        '''
        self.linear_q = nn.Linear(in_features=512, out_features=512, bias=True)
        self.linear_w = nn.Linear(in_features=512, out_features=512, bias=True)
        self.linear_v = nn.Linear(in_features=512, out_features=1)
        self.tanh = nn.Tanh()

        self.action_MLP = nn.Linear(in_features=self.K, out_features=64, bias=True)

        self.softmax = nn.Softmax(dim=1)

        '''
        actor网络
        '''
        if self.has_continuous_action_space:
            self.actor = nn.Sequential(
                nn.Linear(in_features=640, out_features=action_dim),
                nn.Softsign()
            )
        else:
            self.actor = nn.Sequential(
                nn.Linear(in_features=640, out_features=action_dim),
                nn.Softmax(dim=-1)
            )

        '''
        critic网络
        '''
        self.critic = nn.Sequential(
            nn.Linear(in_features=640, out_features=1)
        )

    def set_action_std(self, new_action_std):
        self.action_std = new_action_std
        self.action_var = torch.full((self.action_dim,), new_action_std * new_action_std).to(device)

    def forward(self, obs, position, obs_memory, hid):
        obs = self.conv(obs)
        obs_embedding = obs.reshape((-1, 7 * 7 * 64))
        obs_memory = torch.cat([obs_memory, obs_embedding], dim=2)

        position_con = self.state_embedding(position)

        obs_emb = self.obs_embedding(obs_memory)
        obs_out, hid = self.gru(obs_emb, hid)

        obs_info = self.linear_w(obs_out)
        target_info = self.linear_q(hid).unsqueeze(1)
        obs_target_info = self.tanh(obs_info + target_info)
        alpha = self.linear_v(obs_target_info)
        alpha = self.softmax(alpha)
        memory_info = torch.sum(alpha * obs_emb, dim=1)

        union_info = torch.cat([memory_info, position_con], 1)
        # union_info = torch.cat([obs_con, position_con], 1)

        policy = self.actor(union_info)
        value = self.critic(union_info).squeeze(1)

        return policy, value, obs_embedding, hid.detach()

    def real_act(self, obs, position, obs_memory, hid):
        policy, value, obs_embedding = self.forward(obs, position, obs_memory, hid)
        if not self.has_continuous_action_space:
            policy_dis = Categorical(policy)
            policy = policy_dis.sample()
        return policy, value, obs_embedding

    def act(self, obs, position, obs_memory, hid):
        policy, value, obs_embedding = self.forward(obs, position, obs_memory, hid)
        if self.has_continuous_action_space:
            cov_mat = torch.diag(self.action_var).unsqueeze(0)
            policy_dis = MultivariateNormal(policy, cov_mat)
            action = policy_dis.sample()
            logprob = policy_dis.log_prob(action)
        else:
            policy_dis = Categorical(policy)
            action = policy_dis.sample()
            logprob = policy_dis.log_prob(action)

        return policy.detach(), action.detach(), logprob.detach(), value.detach(), obs_embedding.detach()

    def evaluate(self, obs, position, action, obs_memory, hid):
        policy, value, obs_embedding = self.forward(obs, position, obs_memory, hid)
        if self.has_continuous_action_space:
            action_var = self.action_var.expand_as(policy)
            cov_mat = torch.diag_embed(action_var)
            policy_dis = MultivariateNormal(policy, cov_mat)
            logprobs = policy_dis.log_prob(action)
            dist_entropy = policy_dis.entropy()
        else:
            policy_dis = Categorical(policy)
            logprobs = policy_dis.log_prob(action)
            dist_entropy = policy_dis.entropy()

        return policy_dis, logprobs, dist_entropy, value

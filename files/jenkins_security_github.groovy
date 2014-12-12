import jenkins.model.*
import hudson.security.*
import org.jenkinsci.plugins.*

def github_key = args[0]
def github_secret = args[1]

def instance = Jenkins.getInstance()

def githubRealm = new GithubSecurityRealm(
  'https://github.com',
  'https://api.github.com',
  github_key,
  github_secret
)
instance.setSecurityRealm(githubRealm)

def strategy = new FullControlOnceLoggedInAuthorizationStrategy()
instance.setAuthorizationStrategy(strategy)

instance.save()

import jenkins.model.*
import hudson.security.*
import org.jenkinsci.plugins.*
import com.cloudbees.plugins.credentials.*
import com.cloudbees.plugins.credentials.common.*
import com.cloudbees.plugins.credentials.domains.*
import hudson.plugins.sshslaves.*
import com.cloudbees.jenkins.plugins.sshcredentials.impl.*

if(this.args.length != 4) {
  println("auth_config needs exactly 2 parameters")
  return
}

def api_key = args[0]
def api_secret = args[1]
def username = args[2]
def key_file = args[3]
def private_key = new  File(key_file).text

def instance = Jenkins.getInstance()

println "SEC REALM " + instance.getSecurityRealm().getClass()

global_domain = Domain.global()
            credentials_store =
              Jenkins.instance.getExtensionList(
                'com.cloudbees.plugins.credentials.SystemCredentialsProvider'
              )[0].getStore()

credentials = new BasicSSHUserPrivateKey(
          CredentialsScope.GLOBAL,
          username,
          username,
          new BasicSSHUserPrivateKey.DirectEntryPrivateKeySource(private_key),
          '',
          'An user key'
        )

username_matcher = CredentialsMatchers.withUsername(username)
available_credentials = CredentialsProvider.lookupCredentials(
            StandardUsernameCredentials.class,
            Jenkins.getInstance(),
            hudson.security.ACL.SYSTEM,
            new SchemeRequirement("ssh")
          )
existing_credentials = CredentialsMatchers.firstOrNull(
            available_credentials,
            username_matcher
          )

if (existing_credentials != null) {
    credentials_store.updateCredentials(
                global_domain,
                existing_credentials,
                credentials
              )
} else {
   credentials_store.addCredentials(global_domain, credentials)
}

def githubRealm = new GithubSecurityRealm(
'https://github.com',
'https://api.github.com',
api_key,
api_secret
)
instance.setSecurityRealm(githubRealm)

def strategy = new FullControlOnceLoggedInAuthorizationStrategy()
instance.setAuthorizationStrategy(strategy)

instance.save()

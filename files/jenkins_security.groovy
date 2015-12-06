import groovy.json.JsonSlurper
import jenkins.model.*
import org.jenkinsci.plugins.*
import hudson.security.*

def problems = { msg ->
  println("Problem: " + msg)
  System.exit(1)
}

def github_realm = {instance, key, secret ->
  println("Using GitHub realm")
  def githubRealm = new GithubSecurityRealm(
    'https://github.com',
    'https://api.github.com',
    key,
    secret
  )
  instance.setSecurityRealm(githubRealm)
}

def yolo_strategy = {instance ->  
  println("Using free-for-all auth strategy")
  def strategy = new FullControlOnceLoggedInAuthorizationStrategy()
  instance.setAuthorizationStrategy(strategy)
}

def comitter_strategy = {instance, config ->
  println("using GitHub comitter strategy")
  def strategy = new GithubAuthorizationStrategy(
    config.github_admins.join(','),
    config.github_auth_read,
    config.github_repo_perms,
    config.github_auth_create,
    config.github_orgs.join(','),
    config.github_hooks,
    config.github_cc_read,
    config.github_anon_read)
  instance.setAuthorizationStrategy(strategy)
}

if ( args.length != 1 ) {
  problems("config file required")
}

def config_file = args[0]
File c = new File(config_file)
def config_json = new JsonSlurper().parseText(c.getText())

if ( ! config_json.containsKey('auth_type') ) {
  problems("auth_type missing from config")
}

def instance = Jenkins.getInstance()

if ( config_json.auth_type == "github-free-for-all" ) {
  if ( ! config_json.containsKey('github_key') || ! config_json.containsKey('github_secret') ) {
    problems("github_secret or github_key missing from config")
  }
  github_realm(instance, config_json.github_key, config_json.github_secret)
  yolo_strategy(instance)
} else if ( config_json.auth_type == "github-protected" ) {
  if ( ! config_json.containsKey('github_key') || ! config_json.containsKey('github_secret') ) {
    problems("github_secret or github_key missing from config")
  }
  github_realm(instance, config_json.github_key, config_json.github_secret)
  comitter_strategy(instance, config_json)
} else if ( config_json.auth_type == "none" ) {
  instance.setSecurityRealm(SecurityRealm.NO_AUTHENTICATION)
  instance.setAuthorizationStrategy(new AuthorizationStrategy.Unsecured())
} else {
  problems("Invalid auth_type " + config_json.auth_type)
}

instance.save()

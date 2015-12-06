import groovy.json.JsonSlurper
import jenkins.model.*
import org.jenkinsci.plugins.*

def problems = { msg ->
  println("Problem: " + msg)
  System.exit(1)
}

if ( args.length != 1 ) {
  problems("config file required")
}

def config_file = args[0]
File c = new File(config_file)
def config_json = new JsonSlurper().parseText(c.getText())

def instance = Jenkins.getInstance()

// build a list of envs across all nodes
def props = instance.getGlobalNodeProperties()
def env_nodes = props.getAll(hudson.slaves.EnvironmentVariablesNodeProperty.class)
if ( env_nodes.size() == 0 ) {
  props.add(new hudson.slaves.EnvironmentVariablesNodeProperty())
  env_nodes = props.getAll(hudson.slaves.EnvironmentVariablesNodeProperty.class)  
}

// read env from config file into each node
env_nodes.each { node ->
  config_json.environment.each { k, v ->
    node.getEnvVars().put(k.toString(), v.toString())
  }
}

// set envinject
def envinject_nodes = props.getAll(org.jenkinsci.plugins.envinject.EnvInjectNodeProperty.class)
if ( envinject_nodes.size() == 0 ) {
  props.add(new org.jenkinsci.plugins.envinject.EnvInjectNodeProperty(true, null))
  envinject_nodes = props.getAll(org.jenkinsci.plugins.envinject.EnvInjectNodeProperty.class)
}
// save instance
instance.save()

// location and system email
jlc = jenkins.model.JenkinsLocationConfiguration.get()
jlc.setUrl(config_json.url);
jlc.setAdminAddress(config_json.email);
jlc.save()

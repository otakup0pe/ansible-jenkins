import groovy.json.JsonSlurper
import jenkins.model.*
import org.jenkinsci.plugins.*

def path_file = args[0]

// build path

def instance = Jenkins.getInstance()
def props = instance.getGlobalNodeProperties()
def env_nodes = props.getAll(hudson.slaves.EnvironmentVariablesNodeProperty.class)

if ( env_nodes.size() == 0 ) {
  props.add(new hudson.slaves.EnvironmentVariablesNodeProperty())
  env_nodes = props.getAll(hudson.slaves.EnvironmentVariablesNodeProperty.class)  
}


File f = new File(path_file);
def env_vars = new JsonSlurper().parseText(f.getText())

env_nodes.each { node ->
  env_vars.each { k, v ->
    node.getEnvVars().put(k.toString(), v.toString())
  }
}

// set envinject
def envinject_nodes = props.getAll(org.jenkinsci.plugins.envinject.EnvInjectNodeProperty.class)
if ( envinject_nodes.size() == 0 ) {
  props.add(new org.jenkinsci.plugins.envinject.EnvInjectNodeProperty(true, null))
  envinject_nodes = props.getAll(org.jenkinsci.plugins.envinject.EnvInjectNodeProperty.class)
}

instance.save()

def username = args[0]
def name = args[1]
def email = args[2]
def ssh_key = args[3]

def user = hudson.model.User.get(username)
user.setFullName(name)

def p_email = new hudson.tasks.Mailer.UserProperty(email)
user.addProperty(p_email)

def key_string = new File(ssh_key).getText()
def keys = new org.jenkinsci.main.modules.cli.auth.ssh.UserPropertyImpl(key_string)
user.addProperty(keys)

user.save()

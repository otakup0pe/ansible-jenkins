import jenkins.model.*

def base_url = args[0]
def sys_email = args[1]

jlc = jenkins.model.JenkinsLocationConfiguration.get()
jlc.setUrl(base_url);
jlc.setAdminAddress(sys_email);
jlc.save()
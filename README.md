Jenkins
=======

This Ansible role is meant to provide an (almost) fully configured Jenkins installation. It makes use of Groovy to configure the base system and (optionally) bootstrap jobs via a dedicated git repository and the job-dsl plugin.

While this is used by me both proffesionally and privately it is still a work in progress.

Requirements
------------

This role relies on monit and [ANXS.Monit]<https://github.com/ANXS/monit> from Ansible Galaxy has been chosen.

Role Variables
--------------

* `jenkins_control_user` -- The account name of the user which will be automatically created and used to configure the Jenkins instance.
* `jenkins_control_name` -- The full name of the user which will be automatically created and used to configure the Jenkins instance.
* `jenkins_control_email` -- The email address of the user which will be automatically created and used to configure the Jenkinst instance.
* `jenkins_url` -- The URL that the Jenkins will be accessible at.
* `jenkins_email` -- The email address that will used by Jenkins when it sends messages.
* `jenkins_genesis` -- Whether or not to bootstrap the Jenkins instance with groovy scripts stored in a git repository.
* `jenkins_genesis_template` -- This should point to a Jinja2 template which will be used as the genesis job. The output should be a Jenkins job.
* `jenkins_plugins` -- The list of plugins to be automatically installed
* `jenkins_envinject` -- This is a complex variable which is used to automatically configure the Envinject plugin. It is a list of two part dicts. The `name` and `value` represent both parts of an Environment variable.
* `jenkins_public_key` -- This is the file name of a public SSH key which will be associated with the Jenkins control user
* `jenkins_private_key` -- This is the file name of a private SSH key which will be used by the role to configure the Jenkins instance

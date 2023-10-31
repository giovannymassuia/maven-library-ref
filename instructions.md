- https://medium.com/geekculture/how-to-publish-artifacts-on-maven-central-24342fd286cd

Publishing your first artifact on Maven Central can be as exciting as it can be confusing. We’ll see together how to make your first time as smooth as it should be, while also making sure people can easily contribute to your Open Source Java libraries. At last, we’ll see how to craft a Maven Central-ready CI/CD pipeline via GitHub Actions.

Introduction
Uploading your Java code to Maven Central is an important step towards actively contributing to the Java Open Source community, and it, therefore, mandates you follow a series of quite strict requirements.

Let’s go over each one of these requirements and streamline the process, shall we?

P.S. you can refer to one of the projects I published on Maven Central as a reference in case any doubts arise: https://github.com/dsibilio/badge-maker

Informative POM
The first thing to do is making sure that your groupId matches a domain that you own, or alternatively the domain that is used for sharing your Open Source project.

Assuming I’d own the dsibilio.com domain (which I don’t), and I’d be hosting my project over at GitHub, the following would both be valid groupIds:

com.dsibilio
io.github.dsibilio
The next thing to do is making sure that your pom.xml file includes all of the required information:

licenses
developers
SCM
Filling the tags the right way is quite intuitive, here you can find an example of the tags to include in your POM:


For more examples, you can refer to the Sonatype Metadata Requirements.

Plugins for Days
When publishing artifacts to Maven Central you have to make sure your source code and Javadoc are uploaded as well. You can achieve this by adding the following section to your pom.xml:


Furthermore, you need to sign your artifacts before releasing them. I suggest making a different Maven profile for this as it would make it impossible for other people to contribute to your library as they wouldn’t and shouldn’t know your signature private key/passphrase.

For example:


This snippet makes it so that the signing process be triggered only when building our library with the flag -Pci-cd. Quite handy!

NOTE: the pinentry-mode=loopback is necessary to avoid GPG prompting manual entry of the GPG passphrase. This is crucial when running your build on a remote agent!

Requesting access to Maven Central
Make sure your project is looking good, it’s time to request permission to publish it on Maven Central!

You need to register an account over at Sonatype JIRA and create a new issue for this.

Feel free to use the issue I opened as a template, but make sure to fill all the required fields with your data.

Repositories Setup
Finally, you need to include the Sonatype snapshots/staging repositories in your pom.xml as follows:


NOTE: if you need to deploy your artifact through local builds, you should add your Sonatype JIRA credentials to your ~/.m2/settings.xml file as follows:

<servers>
    <server>
      <id>ossrh</id>
      <username>sonatypeUser</username>
      <password>verySecretPassword</password>
    </server>
</servers>
GPG Setup
You’ll have to create and distribute a new GPG key, so start by downloading and installing GnuPG (you can also find the Windows-compatible version, Gpg4win).

You should now follow the Sonatype recommendations to generate your key, manage its expiration, and ultimately send it over to the required keyservers!

Some of the keyservers you should send your public key to are the following:

hkp://pool.sks-keyservers.net
https://pgp.key-server.io/
https://keyserver.ubuntu.com/
https://pgp.mit.edu/
http://keys.gnupg.net/
IMPORTANT: make sure to remember your GPG passphrase/keep it somewhere safe!

If you don’t intend on using GitHub Actions, you can skip the following section. Your project is good enough as it is, you can just run mvn clean deploy -Dgpg.passphrase="myPassphrase" -Pci-cd to build and deploy your artifact to Sonatype snapshots/staging repositories and perform the release process.

CI/CD with GitHub Actions
Building and deploying via GitHub Actions is quite easy but, in this specific scenario, there are a few things to account for.

First of all, you need to obtain your GPG private key. To do so, you should first identify your GPG key and fetch its ID via gpg --list-keys, you can then export the ASCII-armored version of your private key by running:

gpg --armor --export-secret-keys YOUR_KEY_ID > private.gpg
and providing your passphrase when prompted.

You can now proceed to add all of the GitHub Secrets that are required for your build to work. Navigate to your repository and go to Settings > Secrets and add the following key/value pairs:

MAVEN_GPG_PASSPHRASE: your GPG passphrase
MAVEN_GPG_PRIVATE_KEY: the contents of the private.gpg file you just created
OSSRH_USERNAME: your Sonatype username
OSSRH_TOKEN: your Sonatype password
Your secrets should end up looking like this:


You’re now ready to add the CI/CD pipeline to your GitHub project. Add the following.github/workflows/maven.yml file to your project. It will take care of building, signing, and deploying your artifact to the Sonatype repository every time someone pushes changes to the main branch.


NOTE: if your version ends with -SNAPSHOT, the artifact will be deployed to the snapshots repository. If it doesn’t, it will instead be uploaded to the staging repository, where you can ultimately decide to release it whenever you want!

Release Process
Assuming you already built and deployed on Sonatype a non-snapshot artifact, you can now double-check that all the requirements are satisfied and proceed with your first release!


To do so, simply login to the Sonatype Repository Manager with your Sonatype credentials and:

move to the “Staging Repositories” section on the left
choose the staging repository to promote
“Close” the staging repository
“Release” the staging repository
Et voilà, les jeux sont faits!

If all the requirements were satisfied, your staging repository should now be deleted. You have to comment on the Sonatype JIRA issue you first opened confirming that you just performed your first release and wait for Maven Central to synchronize (should take up to 20 minutes, whereas https://search.maven.org/ can take up to 2h to show your newly deployed artifact).

Congratulations on your first release!

Wrap-Up
The process to deploy and release artifacts to Maven Central can be quite convoluted, but many of these steps are one-time only and the others are quick and easy to repeat every time you want to release a new library for the world to use!

If you liked what you just read, please follow me on Medium and Twitter for more articles like this. I’d really appreciate it ;-)
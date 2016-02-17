jgit-rebaser
============

Rebase your git branch with preserving merges __the right way__.

When do I need this?
--------------------

You need this tool if you have a feature branch in git, that includes merge commits, and you want to preserve these merge commits during a rebase.

Why do I need this?
-------------------

The developers of C Git, in their infinite wisdom, made rebasing with preserving merge commits completely unusable. Basically they expect you to redo each conflict resolution that you have already done during the original merge. The standard advice is to use git-rerere to remember conflict resolutions. 

Luckily we have an alternative implementation, JGit. And the JGit developers implemented rebase the right way.

However, the JGit command line does not have support for rebasing yet. So here is this simple tool created around JGit's RebaseCommand. As soon as the JGit command line includes support for rebasing, we will no longer need this tool.

Building
--------

Use Buck:
* buck fetch jgit-rebaser - to get dependencies
* buck build jgit-rebaser - to build the binary

Use Eclipse:
* Fetch the dependencies using buck fetch
* Import the project into Eclipse

Usage
-----

* Set up your Git working copy for rebasing
** Clean working copy
** Switch to the branch that you want to rebase (preferably create a new one)
* Start rebasing
** java -jar jgit-rebaser.jar </path/to/git/workdir> <upstream branch for rebase>
* When you hit a conflict the tool will exit with RebaseResult: STOPPED
** Resolve conflicts with your favorite tools
** git add <resolved files>
** Do not commit!
* Continue rebasing
** java -jar jgit-rebaser.jar </path/to/git/workdir> <upstream branch for rebase> continue
* Repeat the procedure until the tool exits with RebaseResult: OK

Be Aware: Here be Dragons and Bleeding Edges!
---------------------------------------------

USE AT YOUR OWN RISK!

ALWAYS BACKUP FIRST!

The tool only received minimal testing, and it is probably only suited for our own use case. 

Feel free to customize it to fit your needs.

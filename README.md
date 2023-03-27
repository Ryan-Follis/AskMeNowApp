# AskMeNowApp
Repository for the Android Studio code for our ECE 454 Mobile App, AskMeNow.

# IMPORTANT GIT COMMANDS AND THEIR USE:

'git clone' - clones this shared repository and creates a local repository on your own system

'git commit' - creates a commit that includes all of the files you changed

'git pull' - grabs the committed code from this shared repository

'git push' - pushes your commit containing all file changes you made from your local repository
to this shared repository

'git checkout -b branchname' - creates a new branch with the name branchname
'git checkout branchname' - switches the branch you are working on to branchname

'git fetch' followed by 'git rebase' - catch up your local repository to include changes that
other people in the group have committed and pushed to this shared repository

'git stash' followed by 'git pop' - git stash pushes uncommitted changes on a branch in your
local repository onto a stack so you can pull from this shared repository without losing your
changes; git pop then brings back the uncommitted changes to whatever branch you were working
on before the git stash

'git merge' - merges the code from a feature branch (that one of us works on) and the master
branch; this should only be done when we are absolutely sure we have things working on our
individual branches

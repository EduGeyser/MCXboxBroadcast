# MCXboxBroadcast

A Geyser extension that broadcasts an [EduGeyser](https://edugeyser.org) server over Xbox Live.

This shows up to the authenticated accounts friends in-game as a joinable session.

This fork of [rtm516/MCXboxBroadcast](https://github.com/rtm516/MCXboxBroadcast) advertises the NetherNet endpoint of EduGeyser itself, so players connect directly and stay in the Xbox session while they play. There is no transfer step and no separate NetherNet server.

![Example screenshot](https://user-images.githubusercontent.com/5401186/159083033-b965bfba-de17-4708-8979-1f33bfd5fa28.png)

# DISCLAIMER
You use this project at your own risk, the contributors are not responsible for any damage or loss caused by the software. We suggest you use an alt account for running the tool in case the account is banned as we emulate some features of a client which may or may not be against TOS.

## Features
 - Syncing of MOTD and other server details
 - Automatic friend list management
 - Easy Geyser integration (as an extension)
 - Shows as online and playing Minecraft in the Xbox app and website
 - Multi-account support
 - Uploading of a custom image for the account (see below for more info)

## Installation
1. Download the latest release file `MCXboxBroadcastExtension.jar`
2. Drop the extension into the Geyser `extensions` folder
3. Restart the server
4. Wait for the extension to start and present you with an authentication code
   - `To sign in, use a web browser to open the page https://www.microsoft.com/link and enter the code XXXXXXXX to authenticate.`
5. Follow the link and enter the code
6. Login to the account you want to use
7. Send a friend request to the account on Xbox LIVE
8. Check the friends tab ingame and you should see the server listed

## Custom Image
You can add a custom image to the profile page for the account by placing a `screenshot.jpg` in the same directory as the `config.yml`.

The best settings for this image are `1200x675`, quality `90` and chroma subsampling `4:2:0`.

This can take a few minutes to update on the Xbox Live servers and show ingame.

## Commands
Prefix the commands with `/mcxboxbroadcast`

| Command | Description |
| --- | --- |
| `restart` | Restarts the tool |
| `dumpsession` | Dumps the current session data to files for debugging |
| `accounts list` | Lists the accounts that are currently in use and their followers count |
| `accounts add <sub-session-id>` | Adds an account to the list of accounts to use |
| `accounts remove <sub-session-id>` | Removes an account from the list of accounts to use |

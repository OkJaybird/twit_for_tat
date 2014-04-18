Twit_for_Tat_Agents
===================
Agents class game hw 4 experiment twitter bot.

At the end of the day, everyone can only end up following 5 people, and this bot aims to help keep people honest with their intentions. It’s a constantly running program that keeps a queue of who has been following you the longest, and you automagically follow the top 5 people in your queue. If someone drops out, you automagically unfollow them, and they go to the back of the line should they decide to follow you again, making room for the more serious followers. This bot discourages people from backstabbing you at the last moment before a final follower tally is taken on Tuesday, since they will lose a guaranteed follower in you if they try to ditch out.

To provide more control, there are also preference files allowing you to specify people to ALWAYS follow, regardless of their queue position, and people to NEVER follow.

To get the greatest benefit of this app, it must be running all the time. I recommend running it on an AWS free-tier EC2 instance. 

=====================================
Here’s how to use this:
=====================================
To just use the app, everything you need is in the bin directory, so you can just follow the steps below. Also feel free to modify / branch the code though.

1. Create new app on your twitter account: https://apps.twitter.com/app/
2. Under the Permissions tab, change default app permissions to “Read, Write and Access direct messages” 
3. Generate your API keys & Access Token details (needs to occur after permissions change to guarantee the correct token permissions)
4. Change the twitter4j.properties file to contain the four required API strings you just generated.
5. Change the twit_for_tat.properties file to contain your twitter username (without the @ symbol).
6. If you so desire, add usernames (one on each line) to yes_follow.txt / no_follow.txt to always / never follow those users respectively.
7. run java -jar twit_for_tat.jar. The app will alter the twitter4j.properties file to allow for a higher access rate to the twitter API and exit. This is a one-time config step.
8. run java -jar twit_for_tat.jar. Now you’re in business! Leave it alone & continue on with your life worry free!


=====================================
Thanks Jay!
=====================================
Has using this app made you happy? Does it allow you to sleep easy at night? If so, I would really appreciate being added to your always follow list :D (my handle is already in the list currently, but you are of course free to remove it)





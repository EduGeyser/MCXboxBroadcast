package com.rtm516.mcxboxbroadcast.core.configs;

import com.rtm516.mcxboxbroadcast.core.Constants;
import org.spongepowered.configurate.interfaces.meta.defaults.DefaultBoolean;
import org.spongepowered.configurate.interfaces.meta.defaults.DefaultNumeric;
import org.spongepowered.configurate.interfaces.meta.defaults.DefaultString;
import org.spongepowered.configurate.interfaces.meta.range.NumericRange;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Comment;

@ConfigSerializable
public interface CoreConfig {
    @Comment("Core session settings")
    SessionConfig session();

    @Comment("Friend/follower list sync settings")
    FriendSyncConfig friendSync();

    @Comment("Notification settings (e.g., Slack/Discord webhook)")
    NotificationConfig notifications();

    @Comment("Do not change!")
    @SuppressWarnings("unused")
    default int configVersion() {
        return Constants.CONFIG_VERSION;
    }

    @ConfigSerializable
    interface SessionConfig {
        @Comment("""
            The amount of time in seconds to update session information
            Warning: This can be no lower than 20 due to Xbox rate limits""")
        @DefaultNumeric(30)
        @NumericRange(from = 20, to = Integer.MAX_VALUE)
        int updateInterval();
    }

    @ConfigSerializable
    interface FriendSyncConfig {
        @Comment("""
            The amount of time in seconds to update session information
            Warning: This can be no lower than 20 due to Xbox rate limits""")
        @DefaultNumeric(60)
        @NumericRange(from = 20, to = Integer.MAX_VALUE)
        int updateInterval();

        @Comment("Should we automatically follow people that follow us")
        @DefaultBoolean(true)
        boolean autoFollow();

        @Comment("Should we automatically unfollow people that no longer follow us")
        @DefaultBoolean(true)
        boolean autoUnfollow();

        @Comment("Should we automatically send an invite when a friend is added")
        @DefaultBoolean(true)
        boolean initialInvite();

        @Comment("Friend expiry settings")
        ExpiryConfig expiry();

        @ConfigSerializable
        interface ExpiryConfig {
            @Comment("Should we unfriend people that haven't joined the server in a while")
            @DefaultBoolean(false)
            boolean enabled();

            @Comment("The amount of time in days before a friend is considered expired")
            @DefaultNumeric(15)
            @NumericRange(from = 1, to = Integer.MAX_VALUE)
            int days();

            @Comment("How often to check in seconds for expired friends")
            @DefaultNumeric(1800)
            @NumericRange(from = 1, to = Integer.MAX_VALUE)
            int check();
        }
    }

    @ConfigSerializable
    interface NotificationConfig {
        @Comment("Should we send a message to a slack webhook when the session is updated")
        @DefaultBoolean(false)
        boolean enabled();

        @Comment("""
            The webhook url to send the message to
            If you are using discord add "/slack" to the end of the webhook url""")
        @DefaultString("")
        String webhookUrl();

        @Comment("The message to send when the session is expired and needs to be updated")
        @DefaultString("""
            <!here> Xbox Session expired, sign in again to update it.
            
            Use the following link to sign in: %s
            Enter the code: %s""")
        String sessionExpiredMessage();

        @Comment("The message to send when a friend has restrictions in place that prevent them from being friends with our account")
        @DefaultString("""
            %s (%s) has restrictions in place that prevent them from being friends with our account.""")
        String friendRestrictionMessage();
    }
}

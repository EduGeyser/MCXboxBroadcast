package com.rtm516.mcxboxbroadcast.core;

import com.google.gson.JsonParseException;
import com.rtm516.mcxboxbroadcast.core.exceptions.SessionCreationException;
import com.rtm516.mcxboxbroadcast.core.exceptions.SessionUpdateException;
import com.rtm516.mcxboxbroadcast.core.models.session.CreateSessionRequest;
import com.rtm516.mcxboxbroadcast.core.models.session.CreateSessionResponse;
import com.rtm516.mcxboxbroadcast.core.notifications.NotificationManager;
import com.rtm516.mcxboxbroadcast.core.storage.StorageManager;

import java.util.UUID;
import java.util.concurrent.ScheduledExecutorService;

/**
 * Session manager for a sub-session.
 * <p>
 * Each sub-session hosts its own Xbox session that advertises the same signaling
 * endpoint as the primary session. Discovery and signaling are separate, so any
 * account can advertise the signaling of the shared Geyser instance. An own session
 * gives each account its own member slots and its own social reach.
 */
public class SubSessionManager extends SessionManagerCore {
    private final SessionManager parent;

    /**
     * Create a new session manager for a sub-session
     *
     * @param id The id of the sub-session
     * @param parent The parent session manager
     * @param storageManager The storage manager to use for storing data
     * @param notificationManager The notification manager to use for sending messages
     * @param logger The logger to use for outputting messages
     */
    public SubSessionManager(String id, SessionManager parent, StorageManager storageManager, NotificationManager notificationManager, Logger logger) {
        super(storageManager, notificationManager, logger.prefixed("Sub-Session " + id));
        this.parent = parent;
    }

    @Override
    public ScheduledExecutorService scheduledThread() {
        return parent.scheduledThread();
    }

    @Override
    public void init() throws SessionCreationException, SessionUpdateException {
        // Copy the advertised values and the signaling identity from the primary session
        this.sessionInfo = new ExpandedSessionInfo("", "", parent.sessionInfo());
        this.sessionInfo.setNetherNetId(parent.sessionInfo().getNetherNetId());
        this.sessionInfo.setPmsgId(parent.sessionInfo().getPmsgId());

        super.init();
    }

    @Override
    public String getSessionId() {
        return sessionInfo.getSessionId();
    }

    /**
     * Refresh the advertised values from the primary session.
     *
     * @throws SessionUpdateException If the update fails
     */
    public void syncFromParent() throws SessionUpdateException {
        this.sessionInfo.updateSessionInfo(parent.sessionInfo());
        updateSession();
    }

    @Override
    protected boolean handleFriendship() {
        // TODO Some form of force flag just in case the master friends list is full

        // Add the main account
        boolean subAdd = friendManager().addIfRequired(parent.getXuid(), parent.getGamertag());

        // Get the main account to add us
        boolean mainAdd = parent.friendManager().addIfRequired(getXuid(), getGamertag());

        return subAdd || mainAdd;
    }

    @Override
    protected void updateSession() throws SessionUpdateException {
        // Make sure the websocket connection is still active
        checkConnection();

        String responseBody = super.updateSessionInternal(Constants.CREATE_SESSION.formatted(getSessionId()), new CreateSessionRequest(this.sessionInfo, nonces));
        try {
            CreateSessionResponse sessionResponse = Constants.GSON.fromJson(responseBody, CreateSessionResponse.class);

            // Recreate if we have 28/30 session members
            int players = sessionResponse.members().size();
            if (players >= 28) {
                logger.info("Recreating session due to " + players + "/30 players");
                recreateSession();
            }
        } catch (JsonParseException e) {
            throw new SessionUpdateException("Failed to parse session response: " + e.getMessage());
        }
    }

    /**
     * Publish a new session and activity handle. Players in the old session keep playing,
     * and joins through them target the old session until its members leave.
     *
     * @throws SessionUpdateException If the new session could not be created
     */
    private void recreateSession() throws SessionUpdateException {
        this.sessionInfo.setSessionId(UUID.randomUUID().toString());
        nonces.clear();

        try {
            createSession();
        } catch (SessionCreationException e) {
            throw new SessionUpdateException("Failed to recreate the session: " + e.getMessage());
        }
    }
}

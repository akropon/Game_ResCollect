package com.akropon.game_rescollect.game.util;

import com.akropon.game_rescollect.game.objects.Crystal;
import com.akropon.game_rescollect.util.PlayerID;

/**
 * Created by akropon on 16.09.2017.
 */

public interface ICrystalDestroyedHandler {
    void onCrystalDestroyed(PlayerID caller, Crystal crystal);
}

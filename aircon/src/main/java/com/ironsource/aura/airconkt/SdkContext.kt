package com.ironsource.aura.airconkt

import com.ironsource.aura.airconkt.logging.Logger

/**
 * Internal SDK context used to limit creation of public yet internal classes.
 * This class should not be used outside the SDK internal logic.
 */
internal class SdkContext(val logger: Logger)
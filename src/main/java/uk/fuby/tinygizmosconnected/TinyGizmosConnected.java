package uk.fuby.tinygizmosconnected;

import net.fabricmc.api.ModInitializer;
import uk.fuby.tinygizmosconnected.utils.Register;

public class TinyGizmosConnected implements ModInitializer {

	public static String modid = "tiny_gizmos";

	@Override
	public void onInitialize() {
		Register.register(modid);
	}
}
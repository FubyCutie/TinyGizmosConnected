package uk.fuby.tinygizmosconnected.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.client.render.RenderLayer;
import uk.fuby.tinygizmosconnected.utils.Register;

public class TinyGizmosConnectedClient implements ClientModInitializer {


	@Override
	public void onInitializeClient() {
		BlockRenderLayerMap.INSTANCE.putBlock(Register.WALL, RenderLayer.getCutout());
	}
}

package uk.fuby.tinygizmosconnected.utils;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import uk.fuby.tinygizmosconnected.blocks.GizmoBlock;
import uk.fuby.tinygizmosconnected.blocks.WallBlock;

public class Register {

	public static final WallBlock WALL = new WallBlock(FabricBlockSettings
			.of(Material.METAL).hardness(1.0f).requiresTool());
	public static final GizmoBlock GIZMO = new GizmoBlock(FabricBlockSettings
			.of(Material.METAL).hardness(4.0f).requiresTool());
	public static void register(String modid) {
		Registry.register(Registry.BLOCK, new Identifier(modid, "gizmo"), GIZMO);
		Registry.register(Registry.ITEM, new Identifier(modid, "gizmo"),
				new BlockItem(GIZMO, new FabricItemSettings()));
		Registry.register(Registry.BLOCK, new Identifier(modid, "wall"), WALL);
		Registry.register(Registry.ITEM, new Identifier(modid, "wall"),
				new BlockItem(WALL, new FabricItemSettings()));
	}

}

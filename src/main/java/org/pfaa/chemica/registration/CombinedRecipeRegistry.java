package org.pfaa.chemica.registration;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.pfaa.chemica.model.Strength;
import org.pfaa.chemica.processing.TemperatureLevel;
import org.pfaa.chemica.util.ChanceStack;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public class CombinedRecipeRegistry implements RecipeRegistry {

	private Map<String,RecipeRegistry> registries = new HashMap<String,RecipeRegistry>();
	private CombinedMaterialRecipeRegistry materialRegistry = new CombinedMaterialRecipeRegistry();
	
	public void addRegistry(String key, RecipeRegistry registry) {
		registries.put(key, registry);
		materialRegistry.addRegistry(key, registry.getMaterialRecipeRegistry());
	}
	
	public RecipeRegistry getRegistry(String key) {
		return registries.get(key);
	}
	
	public Set<String> getRegistryNames() {
		return Collections.unmodifiableSet(registries.keySet());
	}

	@Override
	public void registerGrindingRecipe(ItemStack input, ItemStack output, List<ChanceStack> secondaries,
			Strength strength) {
		for (RecipeRegistry registry : registries.values()) {
			registry.registerGrindingRecipe(input, output, secondaries, strength);
		}
	}

	@Override
	public void registerCrushingRecipe(ItemStack input, ItemStack output, ChanceStack dust, Strength strength) {
		for (RecipeRegistry registry : registries.values()) {
			registry.registerCrushingRecipe(input, output, dust, strength);
		}
	}

	@Override
	public void registerSmeltingRecipe(ItemStack input, ItemStack output, ItemStack flux, TemperatureLevel temp) {
		for (RecipeRegistry registry : registries.values()) {
			registry.registerSmeltingRecipe(input, output, flux, temp);
		}
	}

	@Override
	public void registerCastingRecipe(ItemStack input, ItemStack output, ItemStack flux, int temp) {
		for (RecipeRegistry registry : registries.values()) {
			registry.registerCastingRecipe(input, output, flux, temp);
		}
	}

	@Override
	public void registerCastingRecipe(FluidStack input, ItemStack output) {
		for (RecipeRegistry registry : registries.values()) {
			registry.registerCastingRecipe(input, output);
		}
	}

	@Override
	public void registerMeltingRecipe(ItemStack input, FluidStack output, int temp) {
		for (RecipeRegistry registry : registries.values()) {
			registry.registerMeltingRecipe(input, output, temp);
		}
	}

	@Override
	public void registerSmeltingRecipe(ItemStack input, FluidStack output, ItemStack flux, TemperatureLevel temp) {
		for (RecipeRegistry registry : registries.values()) {
			registry.registerSmeltingRecipe(input, output, flux, temp);
		}
	}

	@Override
	public void registerAlloyingRecipe(ItemStack output, ItemStack base, List<ItemStack> solutes, int temp) {
		for (RecipeRegistry registry : registries.values()) {
			registry.registerAlloyingRecipe(output, base, solutes, temp);
		}
	}

	@Override
	public void registerAlloyingRecipe(FluidStack output, List<FluidStack> inputs) {
		for (RecipeRegistry registry : registries.values()) {
			registry.registerAlloyingRecipe(output, inputs);
		}
	}

	@Override
	public void registerRoastingRecipe(List<ItemStack> inputs, ItemStack output, int temp) {
		for (RecipeRegistry registry : registries.values()) {
			registry.registerRoastingRecipe(inputs, output, temp);
		}
	}

	@Override
	public void registerAbsorptionRecipe(List<ItemStack> inputs, FluidStack additive, ItemStack output, int temp) {
		for (RecipeRegistry registry : registries.values()) {
			registry.registerAbsorptionRecipe(inputs, additive, output, temp);
		}
	}

	@Override
	public void registerMixingRecipe(FluidStack input, List<ItemStack> additives, FluidStack output, int temp) {
		for (RecipeRegistry registry : registries.values()) {
			registry.registerMixingRecipe(input, additives, output, temp);
		}
	}

	@Override
	public void registerPhysicalSeparationRecipe(ItemStack input, List<ChanceStack> outputs) {
		for (RecipeRegistry registry : registries.values()) {
			registry.registerPhysicalSeparationRecipe(input, outputs);
		}
	}

	@Override
	public void registerMixingRecipe(List<ItemStack> inputs, ItemStack output) {
		for (RecipeRegistry registry : registries.values()) {
			registry.registerMixingRecipe(inputs, output);
		}
	}
	
	@Override
	public MaterialRecipeRegistry getMaterialRecipeRegistry() {
		return this.materialRegistry;
	}
}

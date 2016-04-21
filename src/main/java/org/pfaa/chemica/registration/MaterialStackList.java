package org.pfaa.chemica.registration;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.pfaa.chemica.item.MaterialStack;
import org.pfaa.chemica.model.IndustrialMaterial;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import net.minecraft.item.ItemStack;

public class MaterialStackList extends AbstractList<MaterialStack> {

	private List<MaterialStack> delegate;
	private Set<List<ItemStack>> itemStackLists;
	
	public MaterialStackList(MaterialStack... materialStacks) {
		this.delegate = Lists.newArrayList(materialStacks);
	}
	
	public MaterialStackList(List<MaterialStack> materialStacks) {
		this.delegate = new ArrayList<MaterialStack>(materialStacks);
	}
	
	public MaterialStackList(IndustrialMaterial... materials) {
		List<MaterialStack> stacks = new ArrayList<MaterialStack>(materials.length);
		for (IndustrialMaterial material : materials) {
			stacks.add(new MaterialStack(material));
		}
		this.delegate = stacks;
	}
	
	@Override
	public MaterialStack get(int arg0) {
		return delegate.get(arg0);
	}

	@Override
	public int size() {
		return delegate.size();
	}
	
	private Function<MaterialStack, Set<ItemStack>> getItemStacks = new Function<MaterialStack, Set<ItemStack>>() {
		@Override
		public Set<ItemStack> apply(MaterialStack input) {
			return input.getItemStacks();
		}
	};
	
	public Set<List<ItemStack>> getItemStackLists() {
		if (this.itemStackLists == null) {
			List<Set<ItemStack>> expandedInputs = Lists.transform(this.delegate, getItemStacks);
			this.itemStackLists = Sets.cartesianProduct(expandedInputs);
		}
		return this.itemStackLists;
	}
	
	private Function<MaterialStack, String> getOreDictKey = new Function<MaterialStack, String>() {
		@Override
		public String apply(MaterialStack input) {
			return input.getOreDictKey();
		}
	};
	
	public List<String> getOreDictKeys() {
		return Lists.transform(this.delegate, getOreDictKey);
	}
	
	public List<String> getRepeatedOreDictKeys() {
		List<String> keys = new ArrayList<String>();
		for (MaterialStack stack : this.delegate) {
			keys.addAll(Collections.nCopies(stack.getSize(), stack.getOreDictKey()));	
		}
		return keys;
	}

}
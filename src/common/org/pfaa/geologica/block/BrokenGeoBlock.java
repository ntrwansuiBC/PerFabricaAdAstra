package org.pfaa.geologica.block;

import java.util.ArrayList;
import java.util.List;

import org.pfaa.geologica.GeoSubstance;
import org.pfaa.geologica.GeologicaTextures;
import org.pfaa.geologica.GeoSubstance.Strength;

import net.minecraft.src.Material;

public class BrokenGeoBlock extends GeoBlock {

	public BrokenGeoBlock(int id, Material material, Strength strength) {
		super(id, material, strength);
	}

	@Override
	public String getTextureFile() {
		return GeologicaTextures.BROKEN;
	}

	@Override
	protected float getResistanceForStrength(Strength strength) {
		float resistance = 0;
		switch(strength) {
		case WEAK:
			resistance = 5.0F;
			break;
		case MEDIUM:
			resistance = 10.0F;
			break;
		case STRONG:
			resistance = 15.0F;
			break;
		case VERY_STRONG:
			resistance = 20.0F;
			break;
		default:
		}
		return resistance;
	}

	@Override
	protected float getHardnessForStrength(Strength strength) {
		float hardness = 0;
		switch(strength) {
		case WEAK:
			hardness = 1.0F;
			break;
		case MEDIUM:
			hardness = 2.0F;
			break;
		case STRONG:
			hardness = 2.5F;
			break;
		case VERY_STRONG:
			hardness = 3.0F;
			break;
		default:
		}
		return hardness;
	}

}

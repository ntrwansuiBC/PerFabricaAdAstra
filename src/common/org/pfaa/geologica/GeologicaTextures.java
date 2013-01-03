package org.pfaa.geologica;

import org.pfaa.RegistrationUtils;

public class GeologicaTextures extends RegistrationUtils {
	private static final String TEXTURE_DIR = Geologica.RESOURCE_DIR + "/textures";
	
	public static final String INTACT = TEXTURE_DIR + "/intact.png";
	public static final String BROKEN = TEXTURE_DIR + "/broken.png";
	
	public static void register() {
		registerDeclaredTextures(GeologicaTextures.class);
	}
}

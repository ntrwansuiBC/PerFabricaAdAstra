package org.pfaa.chemica.processing;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.pfaa.chemica.model.Condition;
import org.pfaa.chemica.model.IndustrialMaterial;
import org.pfaa.chemica.model.MaterialState;
import org.pfaa.chemica.model.Mixture;
import org.pfaa.chemica.model.State;

public class Separation extends ConditionedConversion implements MassTransfer {
	private MaterialState<Mixture> input;
	private MaterialState<?> agent;
	private MaterialState<Mixture> separated, residuum;
	private Axis axis;
	private double energy;
	
	protected Separation(MaterialState<Mixture> input) {
		this.input = input;
	}
	
	public MaterialState<Mixture> getInput() {
		return this.input;
	}

	public MaterialState<?> getAgent() {
		return this.agent;
	}
	
	public MaterialState<Mixture> getResiduum() {
		return this.residuum;
	}
	public MaterialState<Mixture> getSeparated() {
		return this.separated;
	}
	
	public Separation with(MaterialState<?> agent) {
		this.agent = agent;
		return this;
	}

	public Separation with(IndustrialMaterial agent) {
		return this.with(MaterialState.of(agent));
	}

	public Separation extracts(MaterialState<?> output) {
		return this.extracts(output.state, output.material);
	}
	
	public Separation extracts(IndustrialMaterial... outputs) {
		return this.extracts(null, outputs);
	}
	
	public Separation extracts(State state, IndustrialMaterial... outputs) {
		Mixture separated = this.input.material.extract(outputs);
		Mixture residuum = this.input.material.without(outputs);
		MaterialState<?> agent = this.getAgent();
		if (agent != null) {
			separated = separated.mix(agent.material, 1.0);
			state = agent.state;
		}
		this.separated = state.of(separated);
		this.residuum = this.input.state.of(residuum);
		return this;
	}
	
	public Axis getAxis() {
		return this.axis;
	}
	
	public Separation by(Axis axis) {
		this.axis = axis;
		return this;
	}

	@Override
	public List<MaterialStoich<?>> getInputs() {
		return Collections.singletonList(MaterialStoich.of(this.input));
	}

	@Override
	public List<MaterialStoich<?>> getOutputs() {
		return Arrays.asList(MaterialStoich.of(this.separated), MaterialStoich.of(this.residuum));
	}

	public Separation given(double energy) {
		this.energy = energy;
		return this;
	}
	
	@Override
	public double getEnergy() {
		return this.energy;
	}

	@Override
	protected Condition deriveCondition() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Separation at(Condition condition) {
		return (Separation)super.at(condition);
	}

	@Override
	public Separation at(int temp) {
		return (Separation)super.at(temp);
	}

	@Override
	public Type getType() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public static Separation of(MaterialState<Mixture> mixture) {
		return new Separation(mixture);
	}
	
	public static Separation of(Mixture mixture) {
		return of(MaterialState.of(mixture));
	}
	
	public static enum Axis {
		DENSITY,
		MOLECULAR_SIZE,
		PARTICLE_SIZE,
		VAPORIZATION_POINT,
		MELTING_POINT,
		SOLUBILITY,
		ADSORPTIVITY,
		CONDUCTIVITY,
		MAGNETIC_SUSCEPTIBILITY
		;
	}

	public static interface Type extends MassTransfer.Type { 		
		Axis getSeparationAxis();
		
		State getInputState();
		State getAddedState();
		State getSeparatedState();
	}

	public static enum Types implements Type {
		// These state transitions can obviously occur by either temperature or pressure changes
		CONDENSATION(Axis.VAPORIZATION_POINT, State.GAS, State.LIQUID),
		VAPORIZATION(Axis.VAPORIZATION_POINT, State.LIQUID, State.GAS),
		DESUBLIMATION(Axis.VAPORIZATION_POINT, State.GAS, State.SOLID),
		SUBLIMATION(Axis.VAPORIZATION_POINT, State.SOLID, State.GAS),
		// Distillation combines vaporization and condensation steps, but we model it as one
		// Cryogenic distillation requires first condensing the gas (modeled separately)
		DISTILLATION(Axis.VAPORIZATION_POINT, State.LIQUID, State.LIQUID),
		DRYING(Axis.VAPORIZATION_POINT, State.SOLID, State.GAS),
		FREEZING(Axis.MELTING_POINT, State.LIQUID, State.SOLID),
		MELTING(Axis.MELTING_POINT, State.SOLID, State.LIQUID),
		
		ABSORPTION(Axis.SOLUBILITY, State.GAS, State.LIQUID, State.LIQUID),
		STRIPPING(Axis.SOLUBILITY, State.LIQUID, State.GAS, State.GAS),
		LIQUID_LIQUID_EXTRACTION(Axis.SOLUBILITY, State.LIQUID, State.LIQUID, State.LIQUID),
		LEACHING(Axis.SOLUBILITY, State.SOLID, State.AQUEOUS, State.AQUEOUS),
		PRECIPITATION(Axis.SOLUBILITY, State.LIQUID, State.SOLID),
		DEGASIFICATION(Axis.SOLUBILITY, State.LIQUID, State.GAS),
		/* Froth flotation: 
		 * Concentrating sulfide ores by froth flotation requires first adsorbing the sulfide mineral
		 * to a "collector", so that the complex has a non-polar surface and thus is floatable.
		 */
		FLOTATION(Axis.SOLUBILITY, State.AQUEOUS, State.GAS, State.SOLID),
		
		LIQUID_ADSORPTION(Axis.ADSORPTIVITY, State.LIQUID, State.LIQUID, State.LIQUID),
		GAS_ADSORPTION(Axis.ADSORPTIVITY, State.GAS, State.LIQUID, State.LIQUID),
		LIQUID_CHROMATOGRAPHY(Axis.ADSORPTIVITY, State.LIQUID, State.LIQUID, State.LIQUID),
		GAS_CHROMATOGRAPHY(Axis.ADSORPTIVITY, State.GAS, State.GAS, State.GAS),
		PRESSURE_SWING_ADSPORPTION(Axis.ADSORPTIVITY, State.GAS, State.GAS),
		
		/* Molecular filters via pressure differential across membrane */
		REVERSE_OSMOSIS(Axis.MOLECULAR_SIZE, State.LIQUID, State.LIQUID),
		GAS_PERMEATION(Axis.MOLECULAR_SIZE, State.GAS, State.GAS),
		
		/* Cyclones, spirals, etc */
		GAS_GRAVITY(Axis.DENSITY, State.GAS, State.GAS),
		LIQUID_GRAVITY(Axis.DENSITY, State.LIQUID, State.LIQUID),
		/* Jigs, shaking tables */
		SOLID_GRAVITY(Axis.DENSITY, State.SOLID, State.SOLID),
		
		/* Vapor-liquid separation vessels */
		LIQUID_FROM_GAS(Axis.DENSITY, State.GAS, State.LIQUID),
		GAS_FROM_LIQUID(Axis.DENSITY, State.LIQUID, State.GAS),
		
		/* Sedimentation, followed by physical phase separation */
		LIQUID_DECANTATION(Axis.DENSITY, State.LIQUID, State.LIQUID),
		SEDIMENTARY_DECANTATION(Axis.DENSITY, State.LIQUID, State.SOLID),
		
		LIQUID_FILTRATION(Axis.PARTICLE_SIZE, State.LIQUID, State.SOLID),
		GAS_FILTRATION(Axis.PARTICLE_SIZE, State.GAS, State.SOLID),
		
		ELECTROSTATIC(Axis.CONDUCTIVITY, State.SOLID, State.SOLID),
		MAGNETIC(Axis.MAGNETIC_SUSCEPTIBILITY, State.SOLID, State.SOLID);
		
		private Axis separationAxis;	
		private State inputState, addedState, separatedState;
		
		private Types(Axis separationAxis, State inputState) {
			this(separationAxis, inputState, inputState);
		}
		
		private Types(Axis separationAxis, State inputState, State separatedState) {
			this(separationAxis, inputState, null, separatedState);
		}
		
		private Types(Axis separationAxis,
				State inputState, State addedState, State separatedState) {
			this.separationAxis = separationAxis;
			this.inputState = inputState;
			this.addedState = addedState;
			this.separatedState = separatedState;
		}
		
		@Override
		public Axis getSeparationAxis() {
			return this.separationAxis;
		}
		
		@Override
		public State getInputState() {
			return this.inputState;
		}

		@Override
		public State getSeparatedState() {
			return this.separatedState;
		}

		@Override
		public State getAddedState() {
			return this.addedState;
		}
	}
}

package frozenblock.wild.mod.fromAccurateSculk;

import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;

public class NewProperties extends Properties {

    public static final EnumProperty<SculkCatalystPhase> SCULK_CATALYST_PHASE = EnumProperty.of("sculk_catalyst_phase", SculkCatalystPhase.class);
    public static final EnumProperty<SculkShriekerPhase> SCULK_SHRIEKER_PHASE = EnumProperty.of("sculk_shrieker_phase", SculkShriekerPhase.class);
    public static final BooleanProperty ORIGINAL_RF = BooleanProperty.of("original_rf");

}

package net.njsharpe.developmentutility;

import org.bukkit.GameMode;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.Nullable;

public class DamageSource {

    public static final DamageSource IN_FIRE = new DamageSource(EntityDamageEvent.DamageCause.FIRE).bypassArmor().setIsFire();
    public static final DamageSource LIGHTNING_BOLT = new DamageSource(EntityDamageEvent.DamageCause.LIGHTNING);
    public static final DamageSource ON_FIRE = new DamageSource(EntityDamageEvent.DamageCause.FIRE_TICK).bypassArmor().setIsFire();
    public static final DamageSource LAVA = new DamageSource(EntityDamageEvent.DamageCause.LAVA).setIsFire();
    public static final DamageSource HOT_FLOOR = new DamageSource(EntityDamageEvent.DamageCause.HOT_FLOOR).setIsFire();
    public static final DamageSource IN_WALL = new DamageSource(EntityDamageEvent.DamageCause.SUFFOCATION).bypassArmor();
    public static final DamageSource CRAMMING = new DamageSource(EntityDamageEvent.DamageCause.CRAMMING).bypassArmor();
    public static final DamageSource DROWN = new DamageSource(EntityDamageEvent.DamageCause.DROWNING).bypassArmor();
    public static final DamageSource STARVE = new DamageSource(EntityDamageEvent.DamageCause.STARVATION).bypassArmor().bypassMagic();
    public static final DamageSource CONTACT = new DamageSource(EntityDamageEvent.DamageCause.CONTACT);
    public static final DamageSource FALL = new DamageSource(EntityDamageEvent.DamageCause.FALL).bypassArmor().setIsFall();
    public static final DamageSource FLY_INTO_WALL = new DamageSource(EntityDamageEvent.DamageCause.FLY_INTO_WALL).bypassArmor();
    public static final DamageSource OUT_OF_WORLD = new DamageSource(EntityDamageEvent.DamageCause.VOID).bypassArmor().bypassInvulnerability();
    public static final DamageSource GENERIC = new DamageSource(EntityDamageEvent.DamageCause.CUSTOM).bypassArmor();
    public static final DamageSource MAGIC = new DamageSource(EntityDamageEvent.DamageCause.MAGIC).bypassArmor().setMagic();
    public static final DamageSource WITHER = new DamageSource(EntityDamageEvent.DamageCause.WITHER).bypassArmor();
    public static final DamageSource FALLING_BLOCK = new DamageSource(EntityDamageEvent.DamageCause.FALLING_BLOCK).damageHelmet();
    public static final DamageSource DRAGON_BREATH = new DamageSource(EntityDamageEvent.DamageCause.DRAGON_BREATH).bypassArmor();
    public static final DamageSource DRY_OUT = new DamageSource(EntityDamageEvent.DamageCause.DRYOUT);
    public static final DamageSource FREEZE = new DamageSource(EntityDamageEvent.DamageCause.FREEZE).bypassArmor();

    private final String name;

    private boolean damageHelmet;
    private boolean bypassArmor;
    private boolean bypassInvulnerability;
    private boolean bypassMagic;
    private float exhaustion = 0.1F;
    private boolean isFireSource;
    private boolean isProjectile;
    private boolean scalesWithDifficulty;
    private boolean isMagic;
    private boolean isExplosion;
    private boolean isFall;
    private boolean noAggro;

    protected DamageSource(EntityDamageEvent.DamageCause cause) {
        this.name = cause.name();
    }

    public boolean isDamageHelmet() {
        return this.damageHelmet;
    }

    protected DamageSource damageHelmet() {
        this.damageHelmet = true;
        return this;
    }

    public boolean isBypassArmor() {
        return this.bypassArmor;
    }

    protected DamageSource bypassArmor() {
        this.bypassArmor = true;
        return this;
    }

    public boolean isBypassInvulnerability() {
        return this.bypassInvulnerability;
    }

    protected DamageSource bypassInvulnerability() {
        this.bypassInvulnerability = true;
        return this;
    }

    public boolean isBypassMagic() {
        return this.bypassMagic;
    }

    protected DamageSource bypassMagic() {
        this.bypassMagic = true;
        return this;
    }

    public float getFoodExhaustion() {
        return this.exhaustion;
    }

    public boolean isFire() {
        return this.isFireSource;
    }

    protected DamageSource setIsFire() {
        this.isFireSource = true;
        return this;
    }

    public boolean isProjectile() {
        return this.isProjectile;
    }

    public DamageSource setProjectile() {
        this.isProjectile = true;
        return this;
    }

    public boolean scalesWithDifficulty() {
        return this.scalesWithDifficulty;
    }

    public DamageSource setScalesWithDifficulty() {
        this.scalesWithDifficulty = true;
        return this;
    }

    public boolean isMagic() {
        return this.isMagic;
    }

    public DamageSource setMagic() {
        this.isMagic = true;
        return this;
    }

    public boolean isExplosion() {
        return this.isExplosion;
    }

    public DamageSource setExplosion() {
        this.isExplosion = true;
        return this;
    }

    public boolean isFall() {
        return this.isFall;
    }

    public DamageSource setIsFall() {
        this.isFall = true;
        return this;
    }

    public boolean isNoAggro() {
        return this.noAggro;
    }

    public DamageSource setNoAggro() {
        this.noAggro = true;
        return this;
    }

    @Nullable
    public Entity getDirectEntity() {
        return this.getEntity();
    }

    @Nullable
    public Entity getEntity() {
        return null;
    }

    public boolean isCreativePlayer() {
        Entity entity = this.getEntity();
        return entity instanceof Player && ((Player) entity).getGameMode().equals(GameMode.CREATIVE);
    }

    @Nullable
    public Vector getSourcePosition() {
        return null;
    }

    @Override
    public String toString() {
        return String.format("DamageSource{name=%s}", this.name);
    }

}

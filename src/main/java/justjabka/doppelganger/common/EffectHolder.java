package justjabka.doppelganger.common;

import org.bukkit.Material;

public enum EffectHolder {
    POTION(Material.POTION),
    SPLASH_POTION(Material.SPLASH_POTION),
    LINGERING_POTION(Material.LINGERING_POTION),
    TIPPED_ARROW(Material.TIPPED_ARROW);

    private final Material material;

    EffectHolder(Material holder) {
        this.material = holder;
    }

    public Material getMaterial() {
        return material;
    }

    @Override
    public String toString() {
        return this.name().toLowerCase();
    }
}

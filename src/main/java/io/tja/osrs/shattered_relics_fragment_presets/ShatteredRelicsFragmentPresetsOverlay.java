package io.tja.osrs.shattered_relics_fragment_presets;

import com.google.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.client.ui.overlay.*;

import java.awt.*;

@Slf4j
public class ShatteredRelicsFragmentPresetsOverlay extends Overlay {

    private final Client client;
    private final ShatteredRelicsFragmentPresetsPlugin plugin;

    @Inject
    private ShatteredRelicsFragmentPresetsOverlay(Client client, ShatteredRelicsFragmentPresetsPlugin plugin) {
        this.client = client;
        this.plugin = plugin;
        setPosition(OverlayPosition.DYNAMIC);
        setLayer(OverlayLayer.ALWAYS_ON_TOP);
        setPriority(OverlayPriority.MED);
    }

    @Override
    public Dimension render(Graphics2D graphics) {
        if (!plugin.showingFragments) {
            return null;
        }

        renderListOverlay(graphics);
        renderFragmentOverlay(graphics);

        return null;
    }

    private void renderListOverlay(Graphics2D graphics) {
        if (plugin.suppressFilterOverlay)
            return;

        for (FragmentData d : plugin.fragmentData) {
            Rectangle intersection = plugin.fragmentListBounds.intersection(d.widgetBounds);

            if (d.isEquipped) {
                graphics.setColor(new Color(0, 255, 0, 20));
            } else {
                graphics.setColor(new Color(0, 255, 255, 50));
            }
            graphics.fillRect(intersection.x, intersection.y, intersection.width, intersection.height);

            if (d.isEquipped) {
                graphics.setColor(new Color(0, 255, 0, 50));
            } else {
                graphics.setColor(new Color(0, 255, 255, 150));
            }

            graphics.fillRect(plugin.fragmentScrollbarInnerBounds.x,
                    (int) (plugin.fragmentScrollbarInnerBounds.y
                            + plugin.fragmentScrollbarInnerBounds.height * d.scrollPercentage),
                    plugin.fragmentScrollbarInnerBounds.width, 2);
        }
    }

    private void renderFragmentOverlay(Graphics2D graphics) {
        boolean allFragmentsEquipped = plugin.activePreset != null
                && plugin.equippedFragmentNames.equals(plugin.activePreset.fragments);

        for (Rectangle r : plugin.presetEquippedFragmentBounds) {
            if (allFragmentsEquipped) {
                graphics.setColor(new Color(0, 255, 0, 100));
            } else {
                graphics.setColor(new Color(255, 255, 0, 100));
            }
            graphics.fillRect(r.x, r.y, r.width, r.height);
        }
    }
}
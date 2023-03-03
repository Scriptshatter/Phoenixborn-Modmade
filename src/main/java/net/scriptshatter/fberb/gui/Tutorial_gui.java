package net.scriptshatter.fberb.gui;

import io.github.cottonmc.cotton.gui.client.BackgroundPainter;
import io.github.cottonmc.cotton.gui.client.LightweightGuiDescription;
import io.github.cottonmc.cotton.gui.widget.WGridPanel;
import io.github.cottonmc.cotton.gui.widget.WLabel;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

public class Tutorial_gui extends LightweightGuiDescription {
    public Tutorial_gui(){
        WGridPanel root = new WGridPanel();
        root.setSize(300, 200);
        WLabel hint_text = new WLabel(Text.translatable("fberb.turnblast.hint").formatted(Formatting.WHITE));
        WLabel hint_text2 = new WLabel(Text.translatable("fberb.turnblast.hint2").formatted(Formatting.WHITE));
        setRootPanel(root);
        root.add(hint_text, 1, 1);
        root.add(hint_text2, 1, 3);
    }

    @Override
    public void addPainters() {
        if (this.rootPanel!=null && !fullscreen) {
            this.rootPanel.setBackgroundPainter(BackgroundPainter.createNinePatch(new Identifier("minecraft", "textures/block/deepslate_bricks.png")));
        }
    }
}

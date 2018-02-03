/*
 * This is free and unencumbered software released into the public domain.
 *
 * Anyone is free to copy, modify, publish, use, compile, sell, or
 * distribute this software, either in source code form or as a compiled
 * binary, for any purpose, commercial or non-commercial, and by any
 * means.
 *
 * In jurisdictions that recognize copyright laws, the author or authors
 * of this software dedicate any and all copyright interest in the
 * software to the public domain. We make this dedication for the benefit
 * of the public at large and to the detriment of our heirs and
 * successors. We intend this dedication to be an overt act of
 * relinquishment in perpetuity of all present and future rights to this
 * software under copyright law.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS BE LIABLE FOR ANY CLAIM, DAMAGES OR
 * OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,
 * ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 *
 * For more information, please refer to <http://unlicense.org/>
 */

package info.tritusk.screenshothelper;

import net.minecraftforge.client.event.ScreenshotEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;

@Mod.EventBusSubscriber
@Mod(modid = "screenshot_helper", name = "ScreenShot Helper", version = "@INJECTED_VERSION@", useMetadata = true, clientSideOnly = true)
public final class ScreenShotCopier {

    private static Logger logger;

    @Mod.EventHandler
    public void load(FMLPreInitializationEvent event) {
        logger = event.getModLog();
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onScreeShot(ScreenshotEvent event) {
        try {
            Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new TransferableImage(event.getImage()), MinecraftScreenShot.INSTANCE);
        } catch (Exception e) {
            logger.catching(e);
        }
    }

    /**
     * Derived from https://coderanch.com/t/333565/java/BufferedImage-System-Clipboard
     * Also refers to https://stackoverflow.com/questions/4552045/copy-bufferedimage-to-clipboard
     */
    private static final class MinecraftScreenShot implements ClipboardOwner {
        static final MinecraftScreenShot INSTANCE = new MinecraftScreenShot();

        @Override
        public void lostOwnership(Clipboard clipboard, Transferable contents) {
            logger.trace("Last screen shot stored in clipboard was cleared");
        }
    }

    /**
     * Derived from https://coderanch.com/t/333565/java/BufferedImage-System-Clipboard
     * Also refers to https://stackoverflow.com/questions/4552045/copy-bufferedimage-to-clipboard
     */
    private static final class TransferableImage implements Transferable {

        private final Image image;

        TransferableImage(@Nonnull Image image) {
            this.image = image;
        }

        @Override
        public DataFlavor[] getTransferDataFlavors() {
            return new DataFlavor[]{DataFlavor.imageFlavor};
        }

        @Override
        public boolean isDataFlavorSupported(DataFlavor flavor) {
            return flavor == DataFlavor.imageFlavor;
        }

        @Override
        public Object getTransferData(DataFlavor flavor) {
            return flavor == DataFlavor.imageFlavor ? this.image : null;
        }
    }
}



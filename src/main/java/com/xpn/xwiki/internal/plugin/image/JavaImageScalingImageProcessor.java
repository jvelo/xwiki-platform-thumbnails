/*
 * See the NOTICE file distributed with this work for additional
 * information regarding copyright ownership.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package com.xpn.xwiki.internal.plugin.image;

import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Iterator;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.plugins.jpeg.JPEGImageWriteParam;
import javax.imageio.stream.ImageOutputStream;

import org.xwiki.component.annotation.Component;

import com.mortennobel.imagescaling.ResampleOp;
import com.xpn.xwiki.plugin.image.ImageProcessor;

/**
 * Image processor based on java-image-scaling.
 * 
 * @version $Id$
 */
@Component
public class JavaImageScalingImageProcessor implements ImageProcessor
{

    @Override
    public Image readImage(InputStream inputStream) throws IOException
    {
        return ImageIO.read(inputStream);
    }

    @Override
    public void writeImage(RenderedImage image, String mimeType, float quality, OutputStream out) throws IOException
    {
        if ("image/jpeg".equals(mimeType)) {
            // Find a JPEG writer.
            ImageWriter writer = null;
            Iterator<ImageWriter> iter = ImageIO.getImageWritersByMIMEType(mimeType);
            if (iter.hasNext()) {
                writer = iter.next();
            }
            JPEGImageWriteParam iwp = new JPEGImageWriteParam(null);
            iwp.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
            iwp.setCompressionQuality(quality);

            // Prepare output file.
            ImageOutputStream ios = ImageIO.createImageOutputStream(out);
            writer.setOutput(ios);

            // Write the image.
            writer.write(null, new IIOImage(image, null, null), iwp);

            // Cleanup.
            ios.flush();
            writer.dispose();
            ios.close();
        } else {
            ImageIO.write(image, "png", out);
        }
    }

    @Override
    public RenderedImage scaleImage(Image image, int width, int height)
    {
        ResampleOp resampleOp = new ResampleOp(width, height);
        BufferedImage newImage = resampleOp.filter((BufferedImage) image, null);
        return (RenderedImage) newImage;
    }

    @Override
    public RenderedImage createThumbnail(Image image, int width, int height, Rectangle boundaries)
    {
        int imageType = BufferedImage.TYPE_4BYTE_ABGR;
        if (image instanceof BufferedImage) {
            imageType = ((BufferedImage) image).getType();
            if (imageType == BufferedImage.TYPE_BYTE_INDEXED || imageType == BufferedImage.TYPE_BYTE_BINARY
                || imageType == BufferedImage.TYPE_CUSTOM) {
                // INDEXED and BINARY: GIFs or indexed PNGs may lose their transparent bits, for safety revert to ABGR.
                // CUSTOM: Unknown image type, fall back on ABGR.
                imageType = BufferedImage.TYPE_4BYTE_ABGR;
            }
        }
        else {
            throw new UnsupportedOperationException();
        }
        BufferedImage original = (BufferedImage) image;

        if (boundaries != null) {
            original =
                original.getSubimage(new Double(boundaries.getX()).intValue(),
                    new Double(boundaries.getY()).intValue(), new Double(boundaries.getWidth()).intValue(), new Double(
                        boundaries.getHeight()).intValue());
        }

        ResampleOp resampleOp = new ResampleOp(width, height);
        BufferedImage newImage = resampleOp.filter(original, null);
        return (RenderedImage) newImage;
    }

    @Override
    public boolean isMimeTypeSupported(String mimeType)
    {
        try {
            return Arrays.asList(ImageIO.getReaderMIMETypes()).contains(mimeType);
        } catch (NoClassDefFoundError e) {
            // Happens on certain systems where the javax.imageio package is not available.
            return false;
        }
    }

}

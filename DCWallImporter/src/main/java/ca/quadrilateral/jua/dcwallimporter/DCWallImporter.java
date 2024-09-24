package ca.quadrilateral.jua.dcwallimporter;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.text.MessageFormat;
import java.util.UUID;

import javax.imageio.ImageIO;

public class DCWallImporter {
    
    private static BufferedImage getScaledImage(BufferedImage image) {
        int initialWidth = image.getWidth();
        int initialHeight = image.getHeight();

        BufferedImage scaledImage = new BufferedImage(initialWidth * 2, initialHeight * 2, BufferedImage.TYPE_4BYTE_ABGR);
        Graphics2D graphics = scaledImage.createGraphics();
        graphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        graphics.drawImage(image, 0, 0, initialWidth * 2, initialHeight * 2, null);

        return scaledImage;
    }

    public static void main(String[] args) throws Exception {
        //Usage source name

        BufferedImage sourceImage = null;

        if (args.length == 3) {
            int sourceSize = Integer.parseInt(args[2]);
            boolean resize = false;
            if (sourceSize == 640) {
                resize = true;
            } else if (sourceSize == 1024) {
                resize = false;
            } else {
                System.out.println("Invalid source size: " + sourceSize);
                System.exit(3);
            }
            final File sourceFile = new File(args[0]);

            if (sourceFile.canRead()) {
                sourceImage = ImageIO.read(sourceFile);

                BufferedImage closeFront = null;
                BufferedImage nearFront = null;
                BufferedImage farFront = null;
                BufferedImage farFrontSecondary = null;

                BufferedImage closeRight = null;
                BufferedImage closeLeft = null;

                BufferedImage nearRight = null;
                BufferedImage nearLeft = null;

                BufferedImage farRight = null;
                BufferedImage farLeft = null;


                if (!resize) {
                    closeFront = sourceImage.getSubimage(524, 292, 224, 268);
                    nearFront = sourceImage.getSubimage(1044, 4, 96, 116);
                    farFront = sourceImage.getSubimage(4, 4, 32, 38);
                    farFrontSecondary = sourceImage.getSubimage(264, 4, 32, 38);

                    closeRight = sourceImage.getSubimage(1044, 292, 64, 422);
                    closeLeft = sourceImage.getSubimage(784, 292, 64, 422);

                    nearRight = sourceImage.getSubimage(264, 292, 64, 268);
                    nearLeft = sourceImage.getSubimage(4, 292, 64, 268);

                    farRight = sourceImage.getSubimage(784, 4, 32, 116);
                    farLeft = sourceImage.getSubimage(524, 4, 32, 116);
                } else {
                    closeFront = getScaledImage(sourceImage.getSubimage(262, 146, 112, 134));
                    nearFront = getScaledImage(sourceImage.getSubimage(522, 2, 48, 58));
                    farFront = getScaledImage(sourceImage.getSubimage(2, 2, 16, 19));
                    farFrontSecondary = getScaledImage(sourceImage.getSubimage(132, 2, 16, 19));

                    closeRight = getScaledImage(sourceImage.getSubimage(522, 146, 32, 211));
                    closeLeft = getScaledImage(sourceImage.getSubimage(392, 146, 32, 211));

                    nearRight = getScaledImage(sourceImage.getSubimage(132, 146, 32, 134));
                    nearLeft = getScaledImage(sourceImage.getSubimage(2, 146, 32, 134));

                    farRight = getScaledImage(sourceImage.getSubimage(392, 2, 16, 58));
                    farLeft = getScaledImage(sourceImage.getSubimage(262, 2, 16, 58));
                }

                File destDirectory = new File("wall_" + args[1]);
                destDirectory.mkdir();

                String destDirectoryPath = destDirectory.getName() + File.separator;

                ImageIO.write(closeFront, "PNG", new File(destDirectoryPath + args[1] + "_close_front.png"));
                ImageIO.write(nearFront, "PNG", new File(destDirectoryPath + args[1] + "_near_front.png"));
                ImageIO.write(farFront, "PNG", new File(destDirectoryPath + args[1] + "_far_front.png"));
                ImageIO.write(farFrontSecondary, "PNG", new File(destDirectoryPath + args[1] + "_far_front_secondary.png"));

                ImageIO.write(closeRight, "PNG", new File(destDirectoryPath + args[1] + "_close_right.png"));
                ImageIO.write(closeLeft, "PNG", new File(destDirectoryPath + args[1] + "_close_left.png"));

                ImageIO.write(nearRight, "PNG", new File(destDirectoryPath + args[1] + "_near_right.png"));
                ImageIO.write(nearLeft, "PNG", new File(destDirectoryPath + args[1] + "_near_left.png"));

                ImageIO.write(farRight, "PNG", new File(destDirectoryPath + args[1] + "_far_right.png"));
                ImageIO.write(farLeft, "PNG", new File(destDirectoryPath + args[1] + "_far_left.png"));

                File controlFile = new File(destDirectoryPath + args[1] + ".xml");

                FileWriter fw = new FileWriter(controlFile);
                BufferedWriter bw = new BufferedWriter(fw);

                final String wallFormatString = "    <wall xOffset=\"{0}\" yOffset=\"{1}\" facing=\"{2}\" distance=\"{3}\" filename=\"{4}\" />";
                final String newline = System.getProperty("line.separator");

                final String testTextFormatString = "wallDefinitionItem = new WallDefinitionItem(\"{0}\", Direction.{1}, Distance.{2});";

                StringBuilder testTextBuffer = new StringBuilder(0xffff);

                StringBuilder xmlFileBuffer = new StringBuilder(0xffff);

                //WallDefinitionItem wallDefinitionItem = new WallDefinitionItem("", Direction.Front, Distance.UpClose);

                String rootTestTestName = "wall_" + args[1] + File.separator + args[1];

                xmlFileBuffer.append(MessageFormat.format("<jua_wall name=\"{0}\" id=\"{1}\">", args[1], UUID.randomUUID().toString())).append(newline);
                xmlFileBuffer.append("  <wallset>").append(newline);
                xmlFileBuffer.append(MessageFormat.format(wallFormatString, 0, 0, "Front", "Up Close", args[1] + "_close_front.png")).append(newline);
                xmlFileBuffer.append(MessageFormat.format(wallFormatString, 0, 0, "Front", "Near By", args[1] + "_near_front.png")).append(newline);
                xmlFileBuffer.append(MessageFormat.format(wallFormatString, 0, 0, "Front", "Far Away", args[1] + "_far_front.png")).append(newline);
                xmlFileBuffer.append(MessageFormat.format(wallFormatString, 0, 0, "Front", "Far Away Secondary", args[1] + "_far_front_secondary.png")).append(newline);
                xmlFileBuffer.append(MessageFormat.format(wallFormatString, 0, 0, "Right", "Up Close", args[1] + "_close_right.png")).append(newline);
                xmlFileBuffer.append(MessageFormat.format(wallFormatString, 0, 0, "Left", "Up Close", args[1] + "_close_left.png")).append(newline);
                xmlFileBuffer.append(MessageFormat.format(wallFormatString, 0, 0, "Right", "Near By", args[1] + "_near_right.png")).append(newline);
                xmlFileBuffer.append(MessageFormat.format(wallFormatString, 0, 0, "Left", "Near By", args[1] + "_near_left.png")).append(newline);
                xmlFileBuffer.append(MessageFormat.format(wallFormatString, 0, 0, "Right", "Far Away", args[1] + "_far_right.png")).append(newline);
                xmlFileBuffer.append(MessageFormat.format(wallFormatString, 0, 0, "Left", "Far Away", args[1] + "_far_left.png")).append(newline);
                xmlFileBuffer.append("  </wallset>").append(newline);
                xmlFileBuffer.append("<testText>").append(newline);

                xmlFileBuffer.append("WallDefinitionItem wallDefinitionItem = null;").append(newline);

                xmlFileBuffer.append(MessageFormat.format(testTextFormatString, rootTestTestName + "_close_front.png", "Front", "UpClose")).append(newline);
                xmlFileBuffer.append(MessageFormat.format(testTextFormatString, rootTestTestName + "_near_front.png", "Front", "NearBy")).append(newline);
                xmlFileBuffer.append(MessageFormat.format(testTextFormatString, rootTestTestName + "_far_front.png", "Front", "FarAway")).append(newline);
                xmlFileBuffer.append(MessageFormat.format(testTextFormatString, rootTestTestName + "_far_front_secondary.png", "Front", "FarAwaySecondary")).append(newline);
                xmlFileBuffer.append(MessageFormat.format(testTextFormatString, rootTestTestName + "_close_right.png", "Right", "UpClose")).append(newline);
                xmlFileBuffer.append(MessageFormat.format(testTextFormatString, rootTestTestName + "_close_left.png", "Left", "UpClose")).append(newline);
                xmlFileBuffer.append(MessageFormat.format(testTextFormatString, rootTestTestName + "_near_right.png", "Right", "NearBy")).append(newline);
                xmlFileBuffer.append(MessageFormat.format(testTextFormatString, rootTestTestName + "_near_left.png", "Left", "NearBy")).append(newline);
                xmlFileBuffer.append(MessageFormat.format(testTextFormatString, rootTestTestName + "_far_right.png", "Right", "FarAway")).append(newline);
                xmlFileBuffer.append(MessageFormat.format(testTextFormatString, rootTestTestName + "_far_left.png", "Left", "FarAway")).append(newline);


                xmlFileBuffer.append("</testText>").append(newline);
                xmlFileBuffer.append("</jua_wall>");

                bw.write(xmlFileBuffer.toString());

                bw.close();

            } else {
                System.out.println("Can not read source file");
                System.exit(2);
            }
        } else {
            System.out.println("Must specify source, name and size arguments");
            System.exit(1);
        }
    }

}

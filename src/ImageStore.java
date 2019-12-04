import processing.core.PApplet;
import processing.core.PImage;

import java.util.*;

/*
ImageStore: to ideally keep track of the images used in our virtual world
 */

final class ImageStore
{
   private static final String QUAKE_ID = "quake";
   private static final int QUAKE_ACTION_PERIOD = 1100;
   private static final int QUAKE_ANIMATION_PERIOD = 100;

   private Map<String, List<PImage>> images;
   private List<PImage> defaultImages;

   private static final int COLOR_MASK = 0xffffff;
   private static final int KEYED_IMAGE_MIN = 5;
   private static final int KEYED_RED_IDX = 2;
   private static final int KEYED_GREEN_IDX = 3;
   private static final int KEYED_BLUE_IDX = 4;

   public ImageStore(PImage defaultImage)
   {
      this.images = new HashMap<>();
      defaultImages = new LinkedList<>();
      defaultImages.add(defaultImage);
   }

   public List<PImage> getImageList(String key)
   {
      return this.images.getOrDefault(key, this.defaultImages);
   }

   public void loadImages(Scanner in, PApplet screen)
   {
      int lineNumber = 0;
      while (in.hasNextLine())
      {
         try
         {
            processImageLine(this.images, in.nextLine(), screen);
         }
         catch (NumberFormatException e)
         {
            System.out.println(String.format("Image format error on line %d",
                    lineNumber));
         }
         lineNumber++;
      }
   }

   private void processImageLine(Map<String, List<PImage>> images,
                                 String line, PApplet screen)
   {
      String[] attrs = line.split("\\s");
      if (attrs.length >= 2)
      {
         String key = attrs[0];
         PImage img = screen.loadImage(attrs[1]);
         if (img != null && img.width != -1)
         {
            List<PImage> imgs = getImages(images, key);
            imgs.add(img);

            if (attrs.length >= KEYED_IMAGE_MIN)
            {
               int r = Integer.parseInt(attrs[KEYED_RED_IDX]);
               int g = Integer.parseInt(attrs[KEYED_GREEN_IDX]);
               int b = Integer.parseInt(attrs[KEYED_BLUE_IDX]);
               setAlpha(img, screen.color(r, g, b), 0);
            }
         }
      }
   }

   private static List<PImage> getImages(Map<String, List<PImage>> images,
                                  String key)
   {
      List<PImage> imgs = images.get(key);
      if (imgs == null)
      {
         imgs = new LinkedList<>();
         images.put(key, imgs);
      }
      return imgs;
   }

   private void setAlpha(PImage img, int maskColor, int alpha) {
      int alphaValue = alpha << 24;
      int nonAlpha = maskColor & COLOR_MASK;
      img.format = PApplet.ARGB;
      img.loadPixels();
      for (int i = 0; i < img.pixels.length; i++) {
         if ((img.pixels[i] & COLOR_MASK) == nonAlpha) {
            img.pixels[i] = alphaValue | nonAlpha;
         }
      }
      img.updatePixels();
   }

   public Train createTrain(String id, Point position,
                            int actionPeriod, int animationPeriod, List<PImage> images)
   {
      return new Train(id, position, images, actionPeriod, animationPeriod);
   }

   public Thief createThief(String id, Point position,
                          int actionPeriod, int animationPeriod, List<PImage> images)
   {
      return new Thief(id, position, images, actionPeriod, animationPeriod);
   }
   public Mines createMines(String id, Point position,
                                  List<PImage> images)
   {
      return new Mines(id, position, images, 0, 0);
   }

   public Player createOcto(String id, Point position, List<PImage> images){
      return new Player(id, position, images, "octo", 0, 10);
   }
   public Rail2 createRail2(String id, Point position, List<PImage> images)
   {
      return new Rail2(id, position, images);
   }

   public TrainStation createTrainStation(String id, Point position,
                                  List<PImage> images)
   {
      return new TrainStation(id, position, images);
   }

   public Obstacle createObstacle(String id, Point position,
                                  List<PImage> images)
   {
      return new Obstacle(id, position, images);
   }

   public Tree createTree(String id, Point position,
                          List<PImage> images)
   {
      return new Tree(id, position, images);
   }
   public Quake createQuake(Point position, List<PImage> images)
   {
      return new Quake(QUAKE_ID, position, images, QUAKE_ACTION_PERIOD, QUAKE_ANIMATION_PERIOD);
   }

   public Rail1 createRail1(String id, Point position, List<PImage> images)
   {
      return new Rail1(id, position, images);
   }

}

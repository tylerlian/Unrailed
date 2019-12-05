import processing.core.PApplet;
import processing.core.PImage;

import javax.sound.sampled.Clip;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Optional;
import java.util.Scanner;


/*
VirtualWorld is our main wrapper
It keeps track of data necessary to use Processing for drawing but also keeps track of the necessary
components to make our world run (eventScheduler), the data in our world (WorldModel) and our
current view (think virtual camera) into that world (WorldView)
 */

public final class VirtualWorld
   extends PApplet
{
   private static final int TIMER_ACTION_PERIOD = 100;

   private static final int VIEW_WIDTH = 720;
   private static final int VIEW_HEIGHT = 720;
   private static final int TILE_WIDTH = 32;
   private static final int TILE_HEIGHT = 32;
   private static final int WORLD_WIDTH_SCALE = 1;
   private static final int WORLD_HEIGHT_SCALE = 1;

   private static final int VIEW_COLS = VIEW_WIDTH / TILE_WIDTH;
   private static final int VIEW_ROWS = VIEW_HEIGHT / TILE_HEIGHT;
   private static final int WORLD_COLS = VIEW_COLS * WORLD_WIDTH_SCALE;
   private static final int WORLD_ROWS = VIEW_ROWS * WORLD_HEIGHT_SCALE;

   private static final String IMAGE_LIST_FILE_NAME = "imagelist";
   private static final String DEFAULT_IMAGE_NAME = "background_default";
   private static final int DEFAULT_IMAGE_COLOR = 0x808080;

   private static final String LOAD_FILE_NAME = "world.sav";

   private static final String FAST_FLAG = "-fast";
   private static final String FASTER_FLAG = "-faster";
   private static final String FASTEST_FLAG = "-fastest";
   private static final double FAST_SCALE = 0.5;
   private static final double FASTER_SCALE = 0.25;
   private static final double FASTEST_SCALE = 0.10;

   private static double timeScale = 1.0;

   private ImageStore imageStore;
   private WorldModel world;
   private WorldView view;
   private static Player player;
   private static Train train;
   private static Rail1 rail1;
   private static Thief thief;
   private EventScheduler scheduler;
   private Optional<Clip> clip = Optional.empty();

   private long next_time;

   public void settings()
   {
      size(VIEW_WIDTH, VIEW_HEIGHT);
   }

   /*
      Processing entry point for "sketch" setup.
   */
   public void setup()
   {
      this.imageStore = new ImageStore(
         createImageColored(TILE_WIDTH, TILE_HEIGHT, DEFAULT_IMAGE_COLOR));
      this.world = new WorldModel(WORLD_ROWS, WORLD_COLS,
         createDefaultBackground(imageStore));
      this.view = new WorldView(VIEW_ROWS, VIEW_COLS, this, world,
         TILE_WIDTH, TILE_HEIGHT);
      this.scheduler = new EventScheduler(timeScale);

      loadImages(IMAGE_LIST_FILE_NAME, imageStore, this);
      loadWorld(world, LOAD_FILE_NAME, imageStore);

      scheduleActions(world, scheduler, imageStore);

      next_time = System.currentTimeMillis() + TIMER_ACTION_PERIOD;
   }

   public void draw()
   {
      long time = System.currentTimeMillis();
      if (time >= next_time)
      {
         this.scheduler.updateOnTime(time);
         next_time = time + TIMER_ACTION_PERIOD;
      }

      view.drawViewport();
   }

   public void keyPressed()
   {
      if (key == CODED)
      {
         int dx = 0;
         int dy = 0;

         switch (keyCode)
         {
            case UP:
               dy = -1;
               break;
            case DOWN:
               dy = 1;
               break;
            case LEFT:
               dx = -1;
               break;
            case RIGHT:
               dx = 1;
               break;
            case SHIFT:
               player.executeActivity(world, imageStore, scheduler);
               break;
            case CONTROL:
               clip = PlayMusic.playMusic("Lost.wav", clip);
               break;
         }
         player.move(world, dx, dy);
      }
   }

   public void mousePressed() {
      if(!world.isOccupied(getPressedPoint())){
         createThiefOnClick();
      }
   }

   public void createThiefOnClick() {
      virtualTrain(world, imageStore, scheduler);
      virtualThief(world, imageStore, getPressedPoint(), scheduler);
   }

   private Point getPressedPoint(){return new Point(mouseX/TILE_WIDTH , mouseY/TILE_HEIGHT);}

   private static Background createDefaultBackground(ImageStore imageStore)
   {
      return new Background(DEFAULT_IMAGE_NAME,
         imageStore.getImageList(DEFAULT_IMAGE_NAME));
   }

   private static PImage createImageColored(int width, int height, int color)
   {
      PImage img = new PImage(width, height, RGB);
      img.loadPixels();
      for (int i = 0; i < img.pixels.length; i++)
      {
         img.pixels[i] = color;
      }
      img.updatePixels();
      return img;
   }

   private static void loadImages(String filename, ImageStore imageStore,
      PApplet screen)
   {
      try
      {
         Scanner in = new Scanner(new File(filename));
         imageStore.loadImages(in, screen);
      }
      catch (FileNotFoundException e)
      {
         System.err.println(e.getMessage());
      }
   }

   private static void virtualOcto(WorldModel world, ImageStore imageStore){
         Player player = imageStore.createOcto("player", new Point(8,0),  imageStore.getImageList("player"));
         VirtualWorld.player = player;
         world.addEntity(player);
   }

   private static void virtualRail1(WorldModel world, ImageStore imageStore){
         Rail1 rail1 = imageStore.createRail1("rail1", new Point(4,4), imageStore.getImageList("rail1"));
         VirtualWorld.rail1 = rail1;
         world.addEntity(rail1);
   }

   private static void virtualTrain(WorldModel world, ImageStore imageStore, EventScheduler scheduler){
         Train train = imageStore.createTrain("train", new Point(4,4), 0, 0, imageStore.getImageList("train"));
         VirtualWorld.train = train;
         world.addEntity(train);
         (train).scheduleActions(scheduler, world, imageStore);
   }

   private static void virtualThief(WorldModel world, ImageStore imageStore, Point pos, EventScheduler scheduler){
         Thief thief = imageStore.createThief("thief", pos, 5500, 0, imageStore.getImageList("thief"));
         VirtualWorld.thief = thief;
         world.addEntity(thief);
         (thief).scheduleActions(scheduler, world, imageStore);
   }

   private static void loadWorld(WorldModel world, String filename,
                                 ImageStore imageStore)
   {
      try
      {
         Scanner in = new Scanner(new File(filename));
         world.load(in, imageStore);
         virtualOcto(world, imageStore);
         virtualRail1(world, imageStore);
      }
      catch (FileNotFoundException e)
      {
         System.err.println(e.getMessage());
      }
   }

   private void scheduleActions(WorldModel world,
                                EventScheduler scheduler, ImageStore imageStore)
   {
      for (Entity entity : world.getEntities())
      {
         //Only start actions for entities that include action (not those with just animations)
         if(entity instanceof ActivityEntity)
            ((ActivityEntity)entity).scheduleActions(scheduler, world, imageStore);
      }
   }

   private static void parseCommandLine(String[] args)
   {
      for (String arg : args)
      {
         switch (arg)
         {
            case FAST_FLAG:
               timeScale = Math.min(FAST_SCALE, timeScale);
               break;
            case FASTER_FLAG:
               timeScale = Math.min(FASTER_SCALE, timeScale);
               break;
            case FASTEST_FLAG:
               timeScale = Math.min(FASTEST_SCALE, timeScale);
               break;
         }
      }
   }

   public static void main(String [] args)
   {
      parseCommandLine(args);
      PApplet.main(VirtualWorld.class);
   }
}

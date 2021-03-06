import processing.core.PImage;

import java.util.*;

/*
WorldModel ideally keeps track of the actual size of our grid world and what is in that world
in terms of entities and background elements
 */

final class WorldModel
{

   private static final String TREE_KEY = "tree";
   private static final int TREE_NUM_PROPERTIES = 5;
   private static final int TREE_ID = 1;
   private static final int TREE_COL = 2;
   private static final int TREE_ROW = 3;
   private static final int TREE_ACTION_PERIOD = 4;

   private static final String OBSTACLE_KEY = "obstacle";
   private static final int OBSTACLE_NUM_PROPERTIES = 4;
   private static final int OBSTACLE_ID = 1;
   private static final int OBSTACLE_COL = 2;
   private static final int OBSTACLE_ROW = 3;

   private static final String TRAINSTATION_KEY = "trainstation";

   private static final String RAIL2_KEY = "rail2";

   private static final String MINES_KEY = "mines";
   private static final int MINES_NUM_PROPERTIES = 4;
   private static final int MINES_ID = 1;
   private static final int MINES_COL = 2;
   private static final int MINES_ROW = 3;

   private static final String RAIL1_KEY = "rail1";
   private static final int RAIL1_NUM_PROPERTIES = 5;
   private static final int RAIL1_ID = 1;
   private static final int RAIL1_COL = 2;
   private static final int RAIL1_ROW = 3;

   private static final String PLAYER_KEY = "player";
   private static final int PLAYER_NUM_PROPERTIES = 7;
   private static final int PLAYER_ID = 1;
   private static final int PLAYER_COL = 2;
   private static final int PLAYER_ROW = 3;
   private static final int PLAYER_LIMIT = 4;
   private static final int PLAYER_ACTION_PERIOD = 5;
   private static final int PLAYER_ANIMATION_PERIOD = 6;

   private static final String BGND_KEY = "background";
   private static final int BGND_NUM_PROPERTIES = 4;
   private static final int BGND_ID = 1;
   private static final int BGND_COL = 2;
   private static final int BGND_ROW = 3;

   private int numRows;
   private int numCols;
   private ImageStore images;
   private Background background[][];
   private Entity occupancy[][];
   public Set<Entity> entities;

   public WorldModel(int numRows, int numCols, Background defaultBackground)
   {
      this.numRows = numRows;
      this.numCols = numCols;
      this.background = new Background[numRows][numCols];
      this.occupancy = new Entity[numRows][numCols];
      this.entities = new HashSet<>();

      for (int row = 0; row < numRows; row++)
      {
         Arrays.fill(this.background[row], defaultBackground);
      }
   }

   public int getNumCols() {
      return numCols;
   }

   public int getNumRows() {
      return numRows;
   }

   public Set<Entity> getEntities()
   {return entities;}

   public static PImage getCurrentImage(Object entity)
   {
      if (entity instanceof Background)
      {
         return ((Background)entity).getImages()
                 .get(((Background)entity).getImageIndex());
      }
      else if (entity instanceof Entity)
      {
         return ((Entity)entity).getImages().get(((Entity)entity).getImageIndex());
      }
      else
      {
         throw new UnsupportedOperationException(
                 String.format("getCurrentImage not supported for %s",
                         entity));
      }
   }

   public Optional<Point> findOpenAround(Point pos)
   {
      for (int dy = -1; dy <= 1; dy++)
      {
         for (int dx = -1; dx <= 1; dx++)
         {
            Point newPt = new Point(pos.getX() + dx, pos.getY() + dy);
            if (this.withinBounds(newPt) &&
                    !this.isOccupied(newPt))
            {
               return Optional.of(newPt);
            }
         }
      }
      return Optional.empty();
   }

   public Optional<Point> findTrackAround(Point pos)
   {
      for (int dy = -1; dy <= 1; dy++)
      {
         for (int dx = -1; dx <= 1; dx++)
         {
            Point newPt = new Point(pos.getX() + dx, pos.getY() + dy);
            if (this.withinBounds(newPt) &&
                    this.isOccupied(newPt))
            {
               Object o = getOccupant(newPt);
               if( o instanceof Rail1 ){
                  return Optional.of(newPt);
               }
            }
         }
      }
      return Optional.empty();
   }

   private boolean processLine(String line,
                               ImageStore imageStore)
   {
      String[] properties = line.split("\\s");
      if (properties.length > 0)
      {
         switch (properties[0])
         {
            case BGND_KEY:
               return this.parseBackground(properties, imageStore);
            case OBSTACLE_KEY:
               return this.parseObstacle(properties,imageStore);
            case TREE_KEY:
               return this.parseTree(properties, imageStore);
            case MINES_KEY:
               return this.parseMines(properties, imageStore);
            case RAIL1_KEY:
               return this.parseRail1(properties, imageStore);
            case TRAINSTATION_KEY:
               return this.parseTrainStation(properties, imageStore);
            case RAIL2_KEY:
               return this.parseRail2(properties, imageStore);
         }
      }

      return false;
   }

   private boolean parseBackground(String[] properties,
                                   ImageStore imageStore)
   {
      if (properties.length == BGND_NUM_PROPERTIES)
      {
         Point pt = new Point(Integer.parseInt(properties[BGND_COL]),
                 Integer.parseInt(properties[BGND_ROW]));
         String id = properties[BGND_ID];
         this.setBackground(pt,
                 new Background(id, imageStore.getImageList(id)));
      }

      return properties.length == BGND_NUM_PROPERTIES;
   }

   private boolean parseObstacle(String[] properties,
                                 ImageStore imageStore)
   {
      if (properties.length == OBSTACLE_NUM_PROPERTIES)
      {
         Point pt = new Point(
                 Integer.parseInt(properties[OBSTACLE_COL]),
                 Integer.parseInt(properties[OBSTACLE_ROW]));
         Entity entity = imageStore.createObstacle(properties[OBSTACLE_ID], pt,
                  imageStore.getImageList(OBSTACLE_KEY));
         this.tryAddEntity( entity);
      }

      return properties.length == OBSTACLE_NUM_PROPERTIES;
   }

   private boolean parseTrainStation(String[] properties,
                                 ImageStore imageStore)
   {
      if (properties.length == OBSTACLE_NUM_PROPERTIES)
      {
         Point pt = new Point(
                 Integer.parseInt(properties[OBSTACLE_COL]),
                 Integer.parseInt(properties[OBSTACLE_ROW]));
         Entity entity = imageStore.createTrainStation(properties[OBSTACLE_ID], pt,
                 imageStore.getImageList(TRAINSTATION_KEY));
         this.tryAddEntity( entity);
      }

      return properties.length == OBSTACLE_NUM_PROPERTIES;
   }

   private boolean parseRail2(String[] properties,
                                     ImageStore imageStore)
   {
      if (properties.length == OBSTACLE_NUM_PROPERTIES)
      {
         Point pt = new Point(
                 Integer.parseInt(properties[OBSTACLE_COL]),
                 Integer.parseInt(properties[OBSTACLE_ROW]));
         Entity entity = imageStore.createRail2(properties[OBSTACLE_ID], pt,
                 imageStore.getImageList(RAIL2_KEY));
         this.tryAddEntity( entity);
      }

      return properties.length == OBSTACLE_NUM_PROPERTIES;
   }

   private boolean parseTree(String[] properties,
                             ImageStore imageStore)
   {
      if (properties.length == TREE_NUM_PROPERTIES)
      {
         Point pt = new Point(Integer.parseInt(properties[TREE_COL]),
                 Integer.parseInt(properties[TREE_ROW]));
         Entity entity = imageStore.createTree(properties[TREE_ID], pt,
                 imageStore.getImageList(TREE_KEY));
         this.tryAddEntity(entity);
      }

      return properties.length == TREE_NUM_PROPERTIES;
   }

   private boolean parseMines(String[] properties,
                                 ImageStore imageStore)
   {
      if (properties.length == MINES_NUM_PROPERTIES)
      {
         Point pt = new Point(Integer.parseInt(properties[MINES_COL]),
                 Integer.parseInt(properties[MINES_ROW]));
         Entity entity = imageStore.createMines(properties[MINES_ID], pt,
                 imageStore.getImageList(MINES_KEY));
         this.tryAddEntity(entity);
      }

      return properties.length == MINES_NUM_PROPERTIES;
   }

   private boolean parseRail1(String[] properties,
                               ImageStore imageStore)
   {
      if (properties.length == OBSTACLE_NUM_PROPERTIES)
      {
         Point pt = new Point(
                 Integer.parseInt(properties[OBSTACLE_COL]),
                 Integer.parseInt(properties[OBSTACLE_ROW]));
         Entity entity = imageStore.createRail2(properties[OBSTACLE_ID], pt,
                 imageStore.getImageList(RAIL1_KEY));
         this.tryAddEntity( entity);
      }

      return properties.length == OBSTACLE_NUM_PROPERTIES;
   }


   private void tryAddEntity(Entity entity)
   {
      if (this.isOccupied(entity.getPosition()))
      {
         // arguably the wrong type of exception, but we are not
         // defining our own exceptions yet
         throw new IllegalArgumentException("position occupied");
      }

      this.addEntity(entity);
   }

   public void addEntity(Entity entity)
   {
      if (this.withinBounds(entity.getPosition()))
      {
         this.setOccupancyCell(entity.getPosition(), entity);
         this.entities.add(entity);
      }
   }

   boolean withinBounds(Point pos)
   {
      return pos.getY() >= 0 && pos.getY() < this.numRows &&
              pos.getX() >= 0 && pos.getX() < this.numCols;
   }


   public boolean isOccupied(Point pos)
   {
      return this.withinBounds(pos) &&
              this.getOccupancyCell(pos) != null;
   }
   public void load(Scanner in, ImageStore imageStore)
   {
      int lineNumber = 0;
      while (in.hasNextLine())
      {
         try
         {
            if (!this.processLine(in.nextLine(), imageStore))
            {
               System.err.println(String.format("invalid entry on line %d",
                       lineNumber));
            }
         }
         catch (NumberFormatException e)
         {
            System.err.println(String.format("invalid entry on line %d",
                    lineNumber));
         }
         catch (IllegalArgumentException e)
         {
            System.err.println(String.format("issue on line %d: %s",
                    lineNumber, e.getMessage()));
         }
         lineNumber++;
      }
   }


   private Entity getOccupancyCell(Point pos)
   {
      return this.occupancy[pos.getY()][pos.getX()];
   }

   private void setOccupancyCell(Point pos,
                                 Entity entity)
   {
      this.occupancy[pos.getY()][pos.getX()] = entity;
   }

   private Background getBackgroundCell(Point pos)
   {
      return this.background[pos.getY()][pos.getX()];
   }

   private void setBackgroundCell(Point pos,
                                  Background background)
   {
      this.background[pos.getY()][pos.getX()] = background;
   }

   public void moveEntity(Entity entity, Point pos)
   {
      Point oldPos = entity.getPosition();
      if (this.withinBounds(pos) && !pos.equals(oldPos))
      {
         this.setOccupancyCell(oldPos, null);
         this.removeEntityAt(pos);
         this.setOccupancyCell( pos, entity);
         entity.setPosition(pos);
      }
   }

   public void moveTrainEntity(Entity entity, Point pos)
   {
      Point oldPos = entity.getPosition();
      if (this.withinBounds(pos) && !pos.equals(oldPos))
      {
         this.setOccupancyCell(oldPos, null);
//         this.removeEntityAt(pos);
         this.setOccupancyCell( pos, entity);
         entity.setPosition(pos);
      }
   }

   public void removeEntity(Entity entity)
   {
      this.removeEntityAt(entity.getPosition());
   }

   private void removeEntityAt(Point pos)
   {
      if (this.withinBounds(pos)
              && this.getOccupancyCell(pos) != null)
      {
         Entity entity = this.getOccupancyCell(pos);

         /* this moves the entity just outside of the grid for
            debugging purposes */
         entity.setPosition(new Point(-1, -1));
         this.entities.remove(entity);
         this.setOccupancyCell(pos, null);
      }
   }

   public Optional<PImage> getBackgroundImage(Point pos)
   {
      if (this.withinBounds(pos))
      {
         return Optional.of(getCurrentImage(this.getBackgroundCell(pos)));
      }
      else
      {
         return Optional.empty();
      }
   }

   private void setBackground(Point pos,
                              Background background)
   {
      if (this.withinBounds(pos))
      {
         this.setBackgroundCell(pos, background);
      }
   }
   public Optional<Entity> getOccupant(Point pos)
   {
      if (this.isOccupied(pos))
      {
         return Optional.of(this.getOccupancyCell(pos));
      }
      else
      {
         return Optional.empty();
      }
   }

   public Optional<Entity> findNearest(Point pos, String thing)
   {
      List<Entity> ofType = new LinkedList<>();
      for (Entity entity : this.entities)
      {
         if (thing.equals(entity.getType()))
         {
            ofType.add(entity);
         }
      }

      return pos.nearestEntity(ofType);
   }

   public List<Entity> findNearestTrack(Point pos, String thing, List<Point> closed)
   {
      List<Entity> ofType = new LinkedList<>();
      for (Entity entity : this.entities)
      {
         if (thing.equals(entity.getType()))
         {
            ofType.add(entity);
         }
      }

      return ofType;
   }
}

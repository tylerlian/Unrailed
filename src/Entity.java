import processing.core.PImage;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import processing.core.PImage;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

interface Entity{
   String getId();
   Point getPosition();
   void setPosition(Point point);
   List<PImage> getImages();
   int getImageIndex();
   String getType();
   PImage getCurrentImage(Entity entity);

}


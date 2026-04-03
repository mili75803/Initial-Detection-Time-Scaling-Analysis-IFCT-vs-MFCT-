/* Decompiler 45ms, total 126ms, lines 316 */
package ln_first_contact_time_withoutUIOriginal;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;
import java.util.concurrent.Semaphore;
import java.util.concurrent.ThreadLocalRandom;
import sim.display3d.Display3D;
import sim.engine.MakesSimState;
import sim.engine.SimState;
import sim.engine.Stoppable;
import sim.field.continuous.Continuous2D;
import sim.field.continuous.Continuous3D;
import sim.field.network.Network;
import sim.portrayal3d.continuous.ContinuousPortrayal3D;
import sim.util.Bag;
import sim.util.Double2D;
import sim.util.Double3D;

public class AgentsSetting extends SimState {
   public Continuous2D yard = new Continuous2D(1.0D, 100.0D, 100.0D);
   public Continuous3D agitatedYard = new Continuous3D(1.0D, 100.0D, 100.0D, 1000.0D);
   public ContinuousPortrayal3D agitatedYardPortrayal;
   public double TEMPERING_CUT_DOWN = 0.99D;
   public double TEMPERING_INITIAL_RANDOM_MULTIPLIER = 10.0D;
   public boolean tempering = true;
   double randomMultiplier = 0.1D;
   public Network buddies = new Network(false);
   public Network DCList = new Network(false);
   public Network Lymphnode = new Network(false);
   public Network CornersNetwork = new Network(false);
   public Network TcellNetwork = new Network(false);
   int[] TotalIteration = new int[]{0};
   int[] count = new int[]{1};
   long[] sumContactTime = new long[]{0L};
   long[] lastFirstContactTime = new long[]{0L};
   LymphNodes LN;
   double volSide = 0.4D;
   double LNSide;
   int noOfSV = 1;
   int noOfLN = 1;
   int noOfTCCloneType;
   int noOfDCCloneType;
   int noOfDC;
   int noOfTCell;
   int noOfVirus = 1;
   int lastAgentID = 0;
   double scale = 1.0D;
   int[] DCFirstDetectTime;
   long[] contactTime;
   double x_left_SV_corner;
   double x_right_SV_corner;
   double y_bottom_SV_corner;
   double y_top_SV_corner;
   double x_left_LN_corner;
   double x_right_LN_corner;
   double y_bottom_LN_corner;
   double y_top_LN_corner;
   double z_front_LN_corner;
   double z_back_LN_corner;
   Bag Virus_all = new Bag();
   Bag DC_all = new Bag();
   Bag TC_all = new Bag();
   double startTime = 0.0D;
   double[] time;
   int fileNum;
   int ContactTimeType;
   
   Display3D display3d;
   public boolean isTempering() {
      return this.tempering;
   }

   public void setTempering(boolean val) {
      this.tempering = val;
   }

   public AgentsSetting(long seed, double LNside, int TotalTC, int TotalDC, int fileNum, int ContactTimeType) {
      super(seed);
      this.noOfDC = TotalDC;
      this.noOfTCell = TotalTC;
      this.LNSide = LNside * 1000.0D;
      this.fileNum = fileNum;
      this.DCFirstDetectTime = new int[TotalTC];
      this.contactTime = new long[TotalTC];
      this.ContactTimeType = ContactTimeType;
      for(int i = 0; i < TotalTC; ++i) {
         this.DCFirstDetectTime[i] = 0;
         this.contactTime[i] = 0L;
      }

   }

    public AgentsSetting(long seed, double LNside, int TotalTC, int TotalDC, int fileNum, int ContactTimeType, 
            ContinuousPortrayal3D agitatedYardPortrayal, Display3D display3d)
    {
        super(seed);
        this.noOfDC = TotalDC;
        this.noOfTCell = TotalTC;
        this.LNSide = LNside*1000;
        this.fileNum = fileNum;
        this.ContactTimeType = ContactTimeType;
        this.agitatedYardPortrayal = agitatedYardPortrayal;
        this.display3d = display3d;
        //Works fine when type  = 1;
        DCFirstDetectTime = new int[TotalTC];
        contactTime = new long[TotalTC];
        for(int i = 0; i<TotalTC; i++)
        {
            DCFirstDetectTime[i] = 0;
            contactTime[i] = 0;
        }
        
    }
    
   public void edge() {
      Bag students = this.CornersNetwork.getAllNodes();

      for(int i = 0; i < students.size(); i += 8) {
         Agent St1 = (Agent)students.get(0 + i);
         Agent St2 = (Agent)students.get(1 + i);
         Agent St3 = (Agent)students.get(2 + i);
         Agent St4 = (Agent)students.get(3 + i);
         Agent St5 = (Agent)students.get(4 + i);
         Agent St6 = (Agent)students.get(5 + i);
         Agent St7 = (Agent)students.get(6 + i);
         Agent St8 = (Agent)students.get(7 + i);
         this.buddies.addEdge(St1, St2, (Object)null);
         this.buddies.addEdge(St1, St4, (Object)null);
         this.buddies.addEdge(St1, St5, (Object)null);
         this.buddies.addEdge(St2, St3, (Object)null);
         this.buddies.addEdge(St2, St6, (Object)null);
         this.buddies.addEdge(St3, St4, (Object)null);
         this.buddies.addEdge(St3, St7, (Object)null);
         this.buddies.addEdge(St4, St8, (Object)null);
         this.buddies.addEdge(St5, St6, (Object)null);
         this.buddies.addEdge(St5, St8, (Object)null);
         this.buddies.addEdge(St6, St7, (Object)null);
         this.buddies.addEdge(St7, St8, (Object)null);
      }

   }

   public void setLN() {
      int fixed_position = 0;
      if (fixed_position == 0) {
         double centerX = this.yard.getWidth() / 2.0D;
         double centerY = this.yard.getHeight() / 2.0D;
         double centerZ = 0.0D;
         this.LN = new LymphNodes(centerX, centerY, centerZ, this.LNSide / 2.0D, this.scale);
         this.LN.ID = this.lastAgentID++;
         this.x_left_LN_corner = this.LN.corner[0].x;
         this.x_right_LN_corner = this.LN.corner[1].x;
         this.y_bottom_LN_corner = this.LN.corner[0].y;
         this.y_top_LN_corner = this.LN.corner[2].y;
         this.z_front_LN_corner = this.LN.corner[0].z;
         this.z_back_LN_corner = this.LN.corner[4].z;
         this.Lymphnode.addNode(this.LN);
         this.buddies.addNode(this.LN);
         this.schedule.scheduleRepeating(this.LN);

         for(int j = 0; j < 8; ++j) {
            this.yard.setObjectLocation(this.LN.corner[j], new Double2D(this.LN.corner[j].x, this.LN.corner[j].y));
            this.CornersNetwork.addNode(this.LN.corner[j]);
            this.buddies.addNode(this.LN.corner[j]);
            this.schedule.scheduleRepeating(this.LN.corner[j]);
         }
      }

   }

   public void setSVandLNcorner() {
      this.x_left_SV_corner = (this.yard.getWidth() - this.yard.getWidth() * this.volSide) / 2.0D;
      this.x_right_SV_corner = this.x_left_SV_corner + this.yard.getWidth() * this.volSide;
      this.y_bottom_SV_corner = (this.yard.getHeight() - this.yard.getHeight() * this.volSide) / 2.0D;
      this.y_top_SV_corner = this.y_bottom_SV_corner + this.yard.getHeight() * this.volSide;
      Bag agents = this.Lymphnode.getAllNodes();
      this.x_left_LN_corner = (this.yard.getWidth() - this.yard.getWidth() * this.LNSide) / 2.0D;
      this.x_right_LN_corner = this.x_left_LN_corner + this.yard.getWidth() * this.LNSide;
      this.y_bottom_LN_corner = (this.yard.getHeight() - this.yard.getHeight() * this.LNSide) / 2.0D;
      this.y_top_LN_corner = this.y_bottom_LN_corner + this.yard.getHeight() * this.LNSide;
   }

   public void setLNCorners() {
      this.x_left_LN_corner = (this.yard.getWidth() - this.yard.getWidth() * this.LNSide / 2.0D) / 2.0D;
      this.x_right_LN_corner = this.x_left_LN_corner + this.yard.getWidth() * this.LNSide / 2.0D;
      this.y_bottom_LN_corner = (this.yard.getHeight() - this.yard.getHeight() * this.LNSide / 2.0D) / 2.0D;
      this.y_top_LN_corner = this.y_bottom_LN_corner + this.yard.getHeight() * this.LNSide / 2.0D;
   }

   public void setDC() {
      Random randX = new Random();
      Random randY = new Random();
      Random randZ = new Random();
      randX.setSeed(System.currentTimeMillis() + (long)ThreadLocalRandom.current().nextInt());
      randY.setSeed(System.currentTimeMillis() + (long)ThreadLocalRandom.current().nextInt());
      randZ.setSeed(System.currentTimeMillis() + (long)ThreadLocalRandom.current().nextInt());

      for(int j = 0; j < this.noOfDC; ++j) {
         DendriticCell DC = new DendriticCell(this.x_left_LN_corner, this.x_right_LN_corner, this.y_bottom_LN_corner, this.y_top_LN_corner, this.LNSide);
         DC.ID = this.lastAgentID++;
         DC.x = this.x_left_LN_corner + (this.x_right_LN_corner - this.x_left_LN_corner) * randX.nextDouble();
         DC.y = this.y_bottom_LN_corner + (this.y_top_LN_corner - this.y_bottom_LN_corner) * randY.nextDouble();
         DC.z = this.z_back_LN_corner + (this.z_front_LN_corner - this.z_back_LN_corner) * randZ.nextDouble();
         this.DC_all.add(DC);
         this.yard.setObjectLocation(DC, new Double2D(DC.x, DC.y));
         this.DCList.addNode(DC);
         this.buddies.addNode(DC);
      }

   }

   public void setTCell() {
      Semaphore sem = new Semaphore(1);
      Random randX = new Random();
      Random randY = new Random();
      Random randZ = new Random();
      randX.setSeed(System.currentTimeMillis() + (long)ThreadLocalRandom.current().nextInt());
      randY.setSeed(System.currentTimeMillis() + (long)ThreadLocalRandom.current().nextInt());
      randZ.setSeed(System.currentTimeMillis() + (long)ThreadLocalRandom.current().nextInt());

      for(int j = 0; j < this.noOfTCell; ++j) {
         TCell t_cell = new TCell(this, this.LN, this.LNSide, this.scale, this.DCList, this.noOfTCell, sem, this.TotalIteration, this.fileNum, this.ContactTimeType, this.count, this.DCFirstDetectTime, this.sumContactTime, this.lastFirstContactTime, this.contactTime);
         t_cell.ID = this.lastAgentID++;
         t_cell.localID = j + 1;
         this.TC_all.add(t_cell);
         t_cell.x = this.x_left_LN_corner + (this.x_right_LN_corner - this.x_left_LN_corner) * randX.nextDouble();
         t_cell.y = this.y_bottom_LN_corner + (this.y_top_LN_corner - this.y_bottom_LN_corner) * randY.nextDouble();
         t_cell.z = this.z_back_LN_corner + (this.z_front_LN_corner - this.z_back_LN_corner) * randZ.nextDouble();
         t_cell.setCoordinates(t_cell.x, t_cell.y, t_cell.z, t_cell.path);
         this.yard.setObjectLocation(t_cell, new Double2D(t_cell.x, t_cell.y));

         for(int n = 0; n < t_cell.myDC.size(); ++n) {
            DendriticCell DCagent = (DendriticCell)((DendriticCell)t_cell.myDC.get(n));
            t_cell.distanceWithDC[n] = Math.sqrt(Math.pow(DCagent.x - t_cell.x, 2.0D) + Math.pow(DCagent.y - t_cell.y, 2.0D) + Math.pow(DCagent.z - t_cell.z, 2.0D));
            String var13 = t_cell.distanceWithDC[n] + "";
         }

         Stoppable stoppableObj = this.schedule.scheduleRepeating(t_cell);
         t_cell.stop = stoppableObj;
         this.TcellNetwork.addNode(t_cell);
         this.buddies.addNode(t_cell);
      }

   }

   public void fileWrite(String mycontent, int ID) {
      BufferedWriter bw = null;

      try {
         File file = new File("Distance" + ID + this.fileNum + ".txt");
         if (!file.exists()) {
            file.createNewFile();
         }

         FileWriter fw = new FileWriter(file, true);
         bw = new BufferedWriter(fw);
         bw.write(mycontent);
         bw.newLine();
         bw.close();
      } catch (IOException var14) {
         var14.printStackTrace();
      } finally {
         try {
            if (bw != null) {
               bw.close();
            }
         } catch (Exception var13) {
            System.out.println("Error in closing the BufferedWriter" + var13);
         }

      }

   }

   public void start() {
      super.start();
      this.yard.clear();
      this.buddies.clear();
      this.agitatedYard.clear();
      this.setLN();
      this.setDC();
      this.load3DLNandDC();
      this.setTCell();
   }

   public void load3DLNandDC() {
      Bag agents = this.Lymphnode.getAllNodes();

      for(int i = 0; i < agents.size(); ++i) {
         Agent agent = (Agent)((Agent)agents.get(i));
         LymphNodes ln = (LymphNodes)agent;

         for(int j = 0; j < 8; ++j) {
            Double2D locCorner = this.yard.getObjectLocation(ln.corner[j]);
            this.agitatedYard.setObjectLocation(ln.corner[j], new Double3D(locCorner, ln.corner[j].z));
         }

         this.edge();
      }

      Bag agentsDC = this.DCList.getAllNodes();

      for(int i = 0; i < agentsDC.size(); ++i) {
         Agent agent = (Agent)((Agent)agentsDC.get(i));
         Double2D loc = this.yard.getObjectLocation(agent);
         this.agitatedYard.setObjectLocation(agent, new Double3D(loc, agent.z));
      }

   }

   public void load3DStudents() {
      Bag agents = this.TcellNetwork.getAllNodes();

      for(int i = 0; i < agents.size(); ++i) {
         Agent agent = (Agent)((Agent)agents.get(i));
         Double2D loc = this.yard.getObjectLocation(agent);
         this.agitatedYard.setObjectLocation(agent, new Double3D(loc, agent.z));
      }

   }

   public static void main(String[] args) {
      final Class c = AgentsSetting.class;
      args = new String[5];
      args[0] = "2"; args[1] = "21"; args[2] = "5"; args[3] = "1"; args[4] = "0";
      //${LNlength[j]} ${TotalTC[j]} ${TotalDC[j]} ${fileNum} ${Initial = 0 or Mean = 1}

      doLoop(new MakesSimState() {
         public SimState newInstance(long seed, String[] args) {
            try {
                
                
               return (SimState)((SimState)c.getConstructor(Long.TYPE, Double.TYPE, Integer.TYPE, Integer.TYPE, Integer.TYPE, Integer.TYPE).newInstance(seed, Double.valueOf(args[0]), Integer.valueOf(args[1]), Integer.valueOf(args[2]), Integer.valueOf(args[3]), Integer.valueOf(args[4])));
            } catch (Exception var5) {
               throw new RuntimeException("Exception occurred while trying to construct the simulation " + c + "\n" + var5);
            }
         }

         public Class simulationClass() {
            return c;
         }
      }, args);
      System.exit(0);
   }
}


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.rosuda.JRI.*;
// NeuralNet에서 입력변수는 표준화 하는 것이 바람직하기 때문에 입력변수를 표준화 하는 함수를 만들었다.
class Scale {
 double mean, std;
 public void DoScale(double x[]){
  int i;
  double sum1=0., sum2=0.;
  for(i=0; i<x.length; i++) {
   sum1 = sum1 + x[i];
   sum2=sum2+x[i]*x[i];
  }
  mean = sum1/x.length;
  std = Math.sqrt((sum2-x.length * mean*mean)/(x.length-1));
  for(i=0; i<x.length; i++) {
   x[i]=(x[i]-mean)/std;  
  }
 }
}
public class Iris {
 
 static int    no[] = new int [150];
 static double SepalLength[] = new double [150];
 static double SepalWidth[] = new double [150];
 static double PetalLength[] = new double [150];
 static double PetalWidth[] = new double [150];
 static String Species[] = new String [150];
 static Rengine ren = new Rengine(null, false, null);
    static void RConn() {
            
   if(!ren.waitForR())
   {
   System.out.println("Cannot load R");
   return ;
   } 
 }
 
 static void dbConn() {
  int i=0;
  try {
         //load jdbc driver
         Class.forName("com.mysql.jdbc.Driver");
         
         //connect mysql server
         StringBuffer url = new StringBuffer("jdbc:mysql://165.246.38.237:8888/Iris");
         url.append("?characterEncoding=utf-8");
         String id = "scscStudent";
         String passwd = "1234";
         
         Connection conn = DriverManager.getConnection(url.toString(), id, passwd);
         
         Statement stmt = conn.createStatement();
         ResultSet rs = stmt.executeQuery("select * from IrisData");
         while(rs.next()) {
          
             no[i] = rs.getInt(1);
             SepalLength[i] = rs.getDouble(2);
             SepalWidth[i] = rs.getDouble(3);
             PetalLength[i] = rs.getDouble(4);
             PetalWidth[i] = rs.getDouble(5);
             Species[i] = rs.getString(6);
             i++;
         }
                    
     } catch (ClassNotFoundException e) {
         e.printStackTrace();
     } catch (SQLException e) {
         e.printStackTrace();
     } 
 }
 
    public static void main(String[] args) {
     
     double SLMean, SWMean, PLMean, PWMean;
     double SLStd, SWStd, PLStd, PWStd;
     
     dbConn();
  
// 입력 변수 표준화 및 각 변수 평균, 표준편차 Keeping
    
     Scale sc = new Scale();
     sc.DoScale(SepalLength);
     SLMean = sc.mean;
     SLStd=sc.std;
     sc.DoScale(SepalWidth);
        SWMean = sc.mean;
     SWStd=sc.std;
      sc.DoScale(PetalLength);
        PLMean = sc.mean;
     PLStd=sc.std;
      sc.DoScale(PetalWidth);
      PWMean = sc.mean;
     PWStd=sc.std; 
     
        RConn();
// 입력변수를 R로 전송한다.
        
  ren.rniAssign("SepalLength", ren.rniPutDoubleArray(SepalLength), 0);
  ren.rniAssign("SepalWidth", ren.rniPutDoubleArray(SepalWidth), 0);
  ren.rniAssign("PetalLength", ren.rniPutDoubleArray(PetalLength), 0);
  ren.rniAssign("PetalWidth", ren.rniPutDoubleArray(PetalWidth), 0);
  ren.rniAssign("Species", ren.rniPutStringArray(Species), 0);
// 입력변수로 50% Training, 50% Testing set으로 나누고 이를 통해 NeuralNet 모형 계산 및 모형의 정오표 출력
  
  REXP rc1=ren.eval("iris<-data.frame(SepalLength, SepalWidth, PetalLength, PetalWidth, Species)");
  REXP rc2=ren.eval("library(nnet)");
  REXP rc3=ren.eval("samp<-c(sample(1:50,25), sample(51:100,25), sample(101:150,25))");
  REXP rc4=ren.eval("iris.tr<-iris[samp,]");
  REXP rc5=ren.eval("iris.te<-iris[-samp,]");
  REXP rc6=ren.eval("nnetOut<-nnet(Species~., data=iris.tr, size = 3)");
  REXP rc60=ren.eval("save(nnetOut,file='d:/nnOut.out')");
  REXP rc7=ren.eval("y<-iris.te$Species");
  REXP rc8=ren.eval("p<-predict(nnetOut,iris.te,type ='class')");
  REXP rc9=ren.eval("sse<-sum(nnetOut$residuals^2)");
  REXP rc10=ren.eval("nnRes<-table(y,p)");
  REXP rc11=ren.eval("a<-as.data.frame(nnRes)");
  REXP rc12=ren.eval("trueSpecies<-as.vector(a$y)");
  REXP rc13=ren.eval("predSpecies<-as.vector(a$p)");
  REXP rc14=ren.eval("Freq<-a$Freq");
  String trueSpecies[]=rc12.asStringArray();
  String predSpecies[]=rc13.asStringArray(); 
  int freq[]=rc14.asIntArray();
  for(int i=0; i<freq.length; i++)
  {
   System.out.println((trueSpecies[i].trim())+" "+predSpecies[i].trim()+" "+freq[i]);
  }
  double sse=rc9.asDouble();
  System.out.println(sse);
// 판별을 원하는 새로운 객체의 Feature 생성 및 표준화 
  
     double DecSL[] ={5.1};
     double DecSW[] ={2.6};
     double DecPL[] ={3.1};
     double DecPW[] ={1.0};
     
     DecSL[0] = (5.1 - SLMean)/SLStd;
        DecSW[0] = (2.6 - SWMean)/SWStd;
        DecPL[0] = (3.1 - PLMean)/PLStd;
        DecPW[0] = (1.0 - PWMean)/PWStd;    
// 새로운 객체를 R로 전송
        
        ren.rniAssign("SepalLength", ren.rniPutDoubleArray(DecSL), 0);
        ren.rniAssign("SepalWidth", ren.rniPutDoubleArray(DecSW), 0);
        ren.rniAssign("PetalLength", ren.rniPutDoubleArray(DecPL), 0);
        ren.rniAssign("PetalWidth", ren.rniPutDoubleArray(DecPW), 0);
        REXP rc100=ren.eval("iris.new<-data.frame(SepalLength, SepalWidth, PetalLength, PetalWidth)");  
        REXP rc101=ren.eval("newp<-predict(nnetOut,iris.new)");
        REXP rc102=ren.eval("ProbSetosa<-newp[1]");
        REXP rc103=ren.eval("ProbVersiColor<-newp[2]");
        REXP rc104=ren.eval("ProbVirginica<-newp[3]");
  
        double ProbSetosa=rc102.asDouble();
        double ProbVersiColor=rc103.asDouble();
        double ProbVirginica=rc104.asDouble();
        
        System.out.println("\nProbSetosa ProbVersiColor ProbVirginica");
        System.out.println(ProbSetosa+" "+ProbVersiColor+" "+ProbVirginica); 
    }
 
}
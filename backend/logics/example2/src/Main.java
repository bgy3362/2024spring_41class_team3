import java.io.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    public static void main(String[] args) {
        boolean isDetected = false;
        String target_file = args[0];                   
        String fixed_file = args[1];
        String buggyFilePath = target_file;
        String fixedFilePath = fixed_file;
        int classStartIndex = 0;

        int firstStartIfIndex = 0;
        int firstEndIfIndex = 0;
        int thirdStartIfIndex = 0;
        int thirdEndIfIndex = 0;

        String firstCondition = "";
        String secondCondition = "";
        String thirdCondition = "";


        try {
            FileReader reader = new FileReader(buggyFilePath);

            int ch;
            StringBuilder content = new StringBuilder();
            while ((ch = reader.read()) != -1) {
                content.append((char) ch);

            }

            String[] codes = content.toString().split("\n");
            ArrayList<String> lines = new ArrayList<>(Arrays.asList(codes));

            int lineSize = lines.size();

            boolean nestedIfFound = false;

            Pattern pattern = Pattern.compile("\\((.*?)\\)");

            for(int i=0; i<lineSize; i++) {
                String line = codes[i];
                if (line.contains("public class")) {
                    classStartIndex = i;
                    continue;
                }


                if (line.contains("if(")) {
                    Matcher matcher1 = pattern.matcher(line);
                    firstStartIfIndex = i;

                    if(matcher1.find()) {
                        firstCondition = matcher1.group(1);
                    }

                    for (int j = i+1; j < lineSize; j++) {
                        String line2 = lines.get(j);

                        if(line2.contains("if(")) {

                            Matcher matcher2 = pattern.matcher(line2);

                            if(matcher2.find()) {
                                secondCondition = matcher2.group(1);
                            }


                            for(int k = j+1; k < lineSize; k++) {
                                String line3 = lines.get(k);

                                if(line3.contains("if(")) {
                                    nestedIfFound = true;
                                    thirdStartIfIndex = k;

                                    Matcher matcher3 = pattern.matcher(line3);

                                    if(matcher3.find()) {
                                        thirdCondition = matcher3.group(1);
                                    }


                                    for(int l = k+1; l < lineSize; l++) {
                                        String line4 = lines.get(l);
                                        if(line4.contains("}")) {
                                            thirdEndIfIndex = l;
                                            break;
                                        }
                                    }

                                    int count = 0;

                                    for(int l = k+1; l < lineSize; l++) {
                                        String line4 = lines.get(l);

                                        if(line4.contains("}")) {
                                            count++;
                                            if(count==3) {
                                                firstEndIfIndex = l;
                                                break;
                                            }
                                        }
                                    }
                                }
                                if(thirdEndIfIndex != 0) break;
                            }
                        }
                        if(thirdEndIfIndex != 0) break;
                    }
                    if (nestedIfFound) {
                        break;
                    }
                }
            }


            if (nestedIfFound) {
                System.out.println("Algorithm 2 found Line "+ thirdStartIfIndex+1);
                lines.set(classStartIndex, "public class Fixed {\n");
                String conditionBody = "";
                for(int i=thirdStartIfIndex+1; i<thirdEndIfIndex; i++) {
                    conditionBody = conditionBody + (lines.get(i) + "\n");
                }

                //System.out.println("The given Java file contains nested if statements." + firstStartIfIndex + ", " + firstEndIfIndex);

                for(int i=firstStartIfIndex; i<=firstEndIfIndex; i++) {
                    lines.set(i, "##MUSTDELETE##");
                }

                lines.set(firstStartIfIndex-1, "\t\tif((" + firstCondition + " && " + secondCondition + ") && " + thirdCondition + ") {\n");
                lines.add(firstStartIfIndex, "\t\t\t" + conditionBody + "\n");
                lines.add(firstStartIfIndex+1, "\t\t}\n");

                lines.removeIf(item -> item.equals("##MUSTDELETE##"));
                int realSize = lines.size();
                for(int i=0; i<realSize; i++) {
                    // System.out.println(i + " : " + lines.get(i));
                }
            } else {
                System.out.println("Not Found");
            }

            reader.close();

            FileWriter fw = new FileWriter(fixedFilePath);
            BufferedWriter writer = new BufferedWriter(fw);
            lines.forEach(item -> {
                try {
                    writer.write(item);
                    writer.write("\n");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
            writer.close();

        } catch(Exception e) {
            System.out.println(e);
        }
    }

}
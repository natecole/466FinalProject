package project.FinalProject466.DocumentClasses;

public class Wine {
    public double fixed_acidity = 0.0;
    public double volatile_aciditiy = 0.0;
    public double citric_acid = 0.0;
    public double residual_sugar = 0.0;
    public double chlorides = 0.0;
    public int free_sulfur_dioxide = 0;
    public int total_sulfur_dioxide = 0;
    public double density = 0.0;
    public double pH = 0.0;
    public double sulphates = 0.0;
    public double alcohol = 0.0;
    public int quality = 0;

    public Wine(){};

    public Wine(double fixed_acidity, double volatile_aciditiy, double citric_acid, 
    double residual_sugar, double chlorides, int free_sulfur_dioxide, int total_sulfur_dioxide,
    double density, double pH, double sulphates, double alcohol){
        this.fixed_acidity = fixed_acidity;
        this.volatile_aciditiy = volatile_aciditiy;
        this.citric_acid = citric_acid;
        this.residual_sugar = residual_sugar;
        this.chlorides = chlorides;
        this.free_sulfur_dioxide = free_sulfur_dioxide;
        this.total_sulfur_dioxide = total_sulfur_dioxide;
        this.density = density;
        this.pH = pH;
        this.sulphates = sulphates;
        this.alcohol = alcohol;
    }

    public Wine(String[] values){
        this.fixed_acidity = Double.parseDouble(values[0]);
        this.volatile_aciditiy = Double.parseDouble(values[1]);
        this.citric_acid = Double.parseDouble(values[2]);
        this.residual_sugar = Double.parseDouble(values[3]);
        this.chlorides = Double.parseDouble(values[4]);
        this.free_sulfur_dioxide = Integer.parseInt(values[5]);
        this.total_sulfur_dioxide = Integer.parseInt(values[6]);
        this.density = Double.parseDouble(values[7]);
        this.pH = Double.parseDouble(values[8]);
        this.sulphates = Double.parseDouble(values[9]);
        this.alcohol = Double.parseDouble(values[10]);
    }

    public int getActualQuality(){
        return quality;
    }

    public int predictQuality(){
        int quality = 0;
        return quality;
    }

    @Override
    public String toString(){
        String res = "";
        res += ">>Wine:\n";
        res += "\tfixed_acidity: " + Double.toString(fixed_acidity);
        res += ", volatile_aciditiy: " + Double.toString(volatile_aciditiy);
        res += ", citric_acid: " + Double.toString(citric_acid);
        res += ", residual_sugar: " + Double.toString(residual_sugar);
        res += ", chlorides: " + Double.toString(chlorides);
        res += ", free_sulfur_dioxide: " + Integer.toString(free_sulfur_dioxide);
        res += ", total_sulfur_dioxide: " + Integer.toString(total_sulfur_dioxide);
        res += ", density: " + Double.toString(density);
        res += ", pH: " + Double.toString(pH);
        res += ", sulphates: " + Double.toString(sulphates);
        res += ", alcohol: " + Double.toString(alcohol)+"\n";
        res += "Quality: " + Integer.toString(quality) + "\n";
        return res;
    }
}

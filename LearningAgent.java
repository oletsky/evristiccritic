package mathcomp.oletsky.reinforcementlearning.markovdecisionnewnov2021;

import mathcomp.oletsky.mathhelper.VectMatr;
import mathcomp.oletsky.randomchooser.RandomChooser;

public class LearningAgent {
    private String[] actions;
    final private int KOL_ACTIONS;
    final private double GAMMA; //Discount rate
    final private double ALPHA; //Rate of learning
    final private double BETA; //Rate of learning
    final private double COEFF; //Parameter of decisiveness
    private final int KOL_STATES; //Number of states
    private int state;
    private int[] counts;

    private double[][] actionValues;
    private double[] stateValues;

    public LearningAgent(String[] actions,
                         double GAMMA,
                         double ALPHA,
                         double BETA,
                         double COEFF,
                         int KOL_STATES,
                         int state) {
        this.actions = actions;
        this.KOL_ACTIONS = actions.length;
        this.GAMMA = GAMMA;
        this.ALPHA = ALPHA;
        this.BETA = BETA;
        this.COEFF = COEFF;
        this.KOL_STATES = KOL_STATES;
        this.state = state;
        counts=new int[KOL_STATES];
        counts[this.state]=0;

        stateValues = new double[KOL_STATES];
        actionValues = new double[KOL_STATES][KOL_ACTIONS];

        //Initializing state values
        for (int st = 0; st < KOL_STATES; st++) {
            stateValues[st] = 100;
        }


        //Initializing action values
        for (int st = 0; st < KOL_STATES; st++) {
            for (int a = 0; a < KOL_ACTIONS; a++) {
                actionValues[st][a] = 50;
            }

        }

    }

    public int chooseAction(int state) {
        double[] probs = RandomChooser.getExponentProbs(COEFF,
                actionValues[state]);
        return RandomChooser.chooseByProps(probs);
    }

    public void correctStateValues(
            int s,
            int s1,
            double reward) {
        double tempDiff = (reward + GAMMA * stateValues[s1] -
                stateValues[s]);
        stateValues[s] += ALPHA * tempDiff;
    }

    public void correctActionValues(
            double reward,
            int s,
            int a,
            int s1
    ) {
        double tempDiff = (reward + GAMMA * stateValues[s1] -
                stateValues[s]);
        actionValues[s][a]+=BETA*tempDiff;

    }

    public void makeFullAction(LinearAlternativeEnv env) {
        int action = this.chooseAction(state);

        var answer=env.respondToAction(state,
                action);
        int newState = answer.getNewState();

        double reward = answer.getReward();

        this.correctStateValues(state,newState,reward);
        this.correctActionValues(reward, state, action, newState);

        this.state=newState;
        counts[this.state]++;
    }

    public int getState(){
        return this.state;
    }

    public double[][] getActionValues() {
        return actionValues;
    }

    public double[] getStateValues() {
        return stateValues;
    }

    public int[] getCounts(){
        return counts;
    }


}





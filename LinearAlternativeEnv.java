package mathcomp.oletsky.reinforcementlearning.markovdecisionnewnov2021;

import mathcomp.oletsky.randomchooser.RandomChooser;
import mathcomp.oletsky.reinforcementlearning.evristiccriticlearning.Environment;

public class LinearAlternativeEnv {
    final private double STAYPROB;
    final private double STEPPROB;
    private double[] rewards;
    private String[] actions;
    final int KOL_STATES;
    final int KOL_ACTIONS;

    public LinearAlternativeEnv(double STAYPROB,
                                double STEPPROB,
                                double[] rewards,
                                String[] actions) {
        this.STAYPROB = STAYPROB;
        this.STEPPROB = STEPPROB;
        this.rewards = rewards;
        this.actions = actions;
        KOL_STATES = rewards.length;
        KOL_ACTIONS=actions.length;
    }

    double transProp(int from, int act, int to) {
        switch (act) {
            case 0: //Stay
                if (from==0) {
                    if (to==0) return STAYPROB;
                    if (to==1) return 1.-STAYPROB;
                    return 0.;
                }
                if (from==KOL_STATES-1) {
                    if (to==KOL_STATES-1) return STAYPROB;
                    if (to==KOL_STATES-2) return 1.-STAYPROB;
                    return 0.;
                }
                if (from==to) return STAYPROB;
                if ((from==to+1)||(from==to-1)){
                    return (1.-STAYPROB)/2.;
                }
                break;
            case 1: //Right
                if (from==0) {
                    if (to==1) return STEPPROB;
                    if (to==0) return 1.-STEPPROB;
                    return 0.;
                }
                if (from==KOL_STATES-1) {
                    if (to==KOL_STATES-1) return 1.;

                    return 0.;
                }
                if (to==from+1) return STEPPROB;
                if (to==from) return 1.-STEPPROB;
                return 0.;

            case 2: //Left
                if (from==KOL_STATES-1) {
                    if (to==KOL_STATES-2) return STEPPROB;
                    if (to==KOL_STATES-1) return 1.-STEPPROB;
                    return 0.;
                }
                if (from==0) {
                    if (to==0) return 1.;
                    return 0.;
                }
                if (to==from-1) return STEPPROB;
                if (to==from) return 1.-STEPPROB;
                return 0.;

            default: throw new RuntimeException("Illegal action");
        }

        return 0.;

    }

    int obtainNewRandomState(int state,
                                    int action){
        double[] prbs=new double[KOL_STATES];
        for (int s1=0; s1<KOL_STATES; s1++) {
            prbs[s1]=transProp(state,action,s1);

        }

        int newState= RandomChooser.chooseByProps(prbs);
        return newState;

    }

    public double formReward(int s,
                             int a,
                             int newS
    ) {
        return rewards[s];
    }

    public  LinearAlternativeEnv.ResultOfAction respondToAction(
            int s,
            int a
    ) {
        int newState=obtainNewRandomState(s,a);
        double reward=formReward(s,a,newState);
        return new LinearAlternativeEnv.ResultOfAction(newState, reward);

    }


    static class ResultOfAction{
        int newState;
        double reward;

        public ResultOfAction(int newState, double reward) {
            this.newState = newState;
            this.reward = reward;
        }

        public int getNewState() {
            return newState;
        }

        public double getReward() {
            return reward;
        }


    }


}

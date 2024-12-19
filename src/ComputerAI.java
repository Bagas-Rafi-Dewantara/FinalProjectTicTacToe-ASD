import java.awt.Point;
import java.util.ArrayList;
import java.util.Random;

public class ComputerAI {

    private Random r;
    private AILevel level;

    public ComputerAI(){
        this.r = new Random();
        this.level = AILevel.Hard;
    }

    public void setLevel(AILevel l){
        this.level = l;
    }

    public Point turn(Board b, Token t, int turn){
        Point p;
        if(this.level == AILevel.Easy){
            p = this.easy(b, t, turn);
        } else if(this.level == AILevel.Medium){
            p = this.medium(b, t, turn);
        } else {
            p = this.hard(b, t, turn);
        }
        return p;
    }

    private Point easy(Board b, Token t, int turn){
        Point p = win(b, t);

        if(p == null){
            p = blockWin(b, t);
        }

        if(p == null){
            p = this.random(b, t);
        }
        System.out.println("Easy mode selected, AI move: " + p.x + "," + p.y);
        return p;
    }

    private Point medium(Board b, Token t, int turn){
        Point p = win(b, t);

        if(p == null){
            p = blockWin(b, t);
        }

        int choice = r.nextInt(2);
        if(choice == 1){
            if(turn % 2 == 0) {
                if(p == null && turn == 4){
                    p = this.defenceDiagnalAttack(b, t);
                }

                if(p == null && turn == 4){
                    p = defenceCornerChooser(b, t);
                }

                if(p == null){
                    p = defenceMoves(b, t);
                }
            } else {
                if(p == null){
                    p = this.chooseRandomCorner(b, t);
                }
            }
        }

        if(p == null){
            p = this.random(b, t);
        }
        System.out.println("Medium mode selected, AI move: " + p.x + "," + p.y);
        return p;
    }

    private Point hard(Board b, Token t, int turn){
        Point p = win(b, t);

        if(p == null){
            p = blockWin(b, t);
        }

        if(turn % 2 == 0) {
            if(p == null && turn == 4){
                p = this.defenceDiagnalAttack(b, t);
            }

            if(p == null && turn == 4){
                p = defenceCornerChooser(b, t);
            }

            if(p == null){
                p = defenceMoves(b, t);
            }
        } else {
            if(p == null){
                p = this.chooseRandomCorner(b, t);
            }
        }

        if(p == null){
            p = this.random(b, t);
        }
        System.out.println("Hard mode selected, AI move: " + p.x + "," + p.y);
        return p;
    }

    private boolean equalsOpponent(Token boardToken, Token t){
        return boardToken != t && boardToken != Token.Empty;
    }

    private Point defenceDiagnalAttack(Board b, Token t){
        if(this.equalsOpponent(b.getCellType(0, 0), t) && this.equalsOpponent(b.getCellType(2, 2), t) ||
                this.equalsOpponent(b.getCellType(0, 2), t) && this.equalsOpponent(b.getCellType(2, 0), t)) {
            ArrayList<Point> options = new ArrayList<>();
            for(int k = -1; k < 2; k += 2){
                if(1 + k >= 0 && 1 + k < 3) {
                    if(b.getCellType(1, 1 + k) == Token.Empty){
                        options.add(new Point(1, 1 + k));
                    }

                    if(b.getCellType(1 + k, 1) == Token.Empty){
                        options.add(new Point(1 + k, 1));
                    }
                }
            }

            if(!options.isEmpty()) {
                return options.get(this.r.nextInt(options.size()));
            }
        }
        return null;
    }

    private Point defenceCornerChooser(Board b, Token t){
        ArrayList<Point> options = new ArrayList<>();
        int sum;
        for(int i = 0; i < 3; i += 2){
            for(int j = 0; j < 3; j += 2){
                sum = 0;
                if(b.getCellType(i, j) == Token.Empty){
                    for(int k = -1; k < 2; k += 2){
                        if(j + k >= 0 && j + k < 3) {
                            if(b.getCellType(i, j + k) == t){
                                sum--;
                            } else if(b.getCellType(i, j + k) != Token.Empty){
                                sum++;
                            }
                        }
                    }

                    for(int k = -1; k < 2; k += 2){
                        if(i + k >= 0 && i + k < 3) {
                            if(b.getCellType(i + k, j) == t){
                                sum--;
                            } else if(b.getCellType(i + k, j) != Token.Empty){
                                sum++;
                            }
                        }
                    }
                    if(sum == 2){
                        options.add(new Point(i, j));
                    }
                }
            }
        }

        if(!options.isEmpty()) {
            return options.get(this.r.nextInt(options.size()));
        }
        return null;
    }

    private Point defenceMoves(Board b, Token t){
        if(b.getCellType(1, 1) == Token.Empty){
            return new Point(1, 1);
        } else {
            return this.chooseRandomCorner(b, t);
        }
    }

    private Point chooseRandomCorner(Board b, Token t){
        ArrayList<Point> options = new ArrayList<>();
        for(int i = 0; i < 3; i += 2){
            for(int j = 0; j < 3; j += 2){
                if(b.getCellType(i, j) == Token.Empty){
                    options.add(new Point(i, j));
                }
            }
        }

        if(!options.isEmpty()) {
            return options.get(this.r.nextInt(options.size()));
        }
        return null;
    }

    private Point win(Board b, Token t){
        // Logic remains the same, using b.getCellType instead of c.getType()
        return null; // Placeholder for actual implementation
    }

    Point blockWin(Board b, Token t) {
        // Logic remains the same, using b.getCellType instead of c.getType()
        return null; // Placeholder for actual implementation
    }

    Point random(Board b, Token t) {
        ArrayList<Point> options = new ArrayList<>();
        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 3; j++){
                if(b.getCellType(i, j) == Token.Empty){
                    options.add(new Point(i, j));
                }
            }
        }

        return options.get(this.r.nextInt(options.size()));
    }
}

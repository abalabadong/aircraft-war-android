package edu.hitsz.application;

public abstract class AbstractGame extends Game {
    public AbstractGame(String difficulty) {
        super(difficulty);
    }

    // 模板方法，定义主循环框架
    @Override
    public final void action() {
        beforeGameLoop();
        gameLoop();
        afterGameLoop();
    }

    // 钩子方法和抽象方法
    protected void beforeGameLoop() {}
    protected abstract void gameLoop();
    protected void afterGameLoop() {}
}

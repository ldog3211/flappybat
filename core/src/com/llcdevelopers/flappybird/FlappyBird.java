package com.llcdevelopers.flappybird;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
//import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
//import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
//import com.badlogic.gdx.scenes.scene2d.ui.Button;
//import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
//import com.badlogic.gdx.utils.Scaling;

import java.util.Random;

public class FlappyBird extends ApplicationAdapter {
	SpriteBatch batch;
	Texture background;
	ShapeRenderer shapeRenderer;

	Texture topTube;
	Texture bottomTube;
	float gap = 500;
	float maxTubeOffset;
	Random randomGenerator;
	float tubeVelocity = 3;
	int numberOfTubes = 4;
	float[] tubeX = new float[numberOfTubes];
	float[] tubeOffset = new float[numberOfTubes];
	float distanceBetweenTubes;

	Texture gameOver;

	Texture[] birds;
	int flapState = 0;
	float birdY = 0;
	float velocity = 0;
	Circle birdCircle;
	Rectangle[] topTubeRectangles;
	Rectangle[] bottomTubeRectangles;

	int gameState = 0;
	float gravity = 2;

	int score = 0;
	int scoringTube = 0;
	BitmapFont font;

	float gameOverY = 0;



	@Override
	public void create () {
		batch = new SpriteBatch();

		background = new Texture("moonbg.png");
		shapeRenderer = new ShapeRenderer();
		birdCircle = new Circle();
		topTubeRectangles = new Rectangle[numberOfTubes];
		bottomTubeRectangles = new Rectangle[numberOfTubes];
		font = new BitmapFont();
		font.setColor(Color.WHITE);
		font.getData().setScale(15);

		gameOver = new Texture("gameovernew.png");

		topTube = new Texture("topcastlenew.png");
		bottomTube = new Texture("bottomcastlenew.png");
		maxTubeOffset = Gdx.graphics.getHeight() / 2 - gap / 2 - 100;
		randomGenerator = new Random();
		distanceBetweenTubes = 700;

		birds = new Texture[2];
		birds[0] = new Texture("bat.png");
		birds[1] = new Texture("bat.png");
		birdY = Gdx.graphics.getHeight() / 2 - birds[0].getHeight() / 2;
		gameOverY = Gdx.graphics.getHeight();

		startGame();

	}

	public void startGame() {
		birdY = Gdx.graphics.getHeight() / 2 - birds[0].getHeight() / 2;

		for (int i = 0; i < numberOfTubes; i++) {
			tubeX[i] = Gdx.graphics.getWidth() - topTube.getWidth() / 2 + i * distanceBetweenTubes + 200;
			tubeOffset[i] = (randomGenerator.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - gap - 200);

			topTubeRectangles[i] = new Rectangle();
			bottomTubeRectangles[i] = new Rectangle();
		}
	}


	@Override
	public void render () {

		batch.begin();
		batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

		batch.draw(birds[flapState], Gdx.graphics.getWidth() / 2 - birds[flapState].getWidth() / 2, birdY);

		if (gameState == 1) {

			if (tubeX[scoringTube] < Gdx.graphics.getWidth() / 3) {

				score++;

				if (scoringTube < numberOfTubes -1) {

					scoringTube++;

				} else {

					scoringTube = 0;

				}

			}

			if (Gdx.input.justTouched()) {

				velocity = -30;
				birds[0] = new Texture("bat2.png");
				birds[1] = new Texture("bat2.png");

			} else if (velocity < 0) {

				birds[0] = new Texture("bat2.png");
				birds[1] = new Texture("bat2.png");

			} else {
				birds[0] = new Texture("bat.png");
				birds[1] = new Texture("bat.png");
			}

			for (int i = 0; i < numberOfTubes; i++) {

				if (tubeX[i] < - topTube.getWidth()) {

					tubeX[i] += numberOfTubes * distanceBetweenTubes;
					tubeOffset[i] = (randomGenerator.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - gap - 200);

				} else {

					tubeX[i] = tubeX[i] - tubeVelocity;
				}

				tubeX[i] = tubeX[i] - tubeVelocity;

				batch.draw(topTube, tubeX[i], Gdx.graphics.getHeight() / 2 + gap / 2 + tubeOffset[i]);
				batch.draw(bottomTube, tubeX[i], Gdx.graphics.getHeight() / 2 - gap / 2 - bottomTube.getHeight() + tubeOffset[i]);

				topTubeRectangles[i] = new Rectangle(tubeX[i] + 100, Gdx.graphics.getHeight() / 2 + gap / 2 + tubeOffset[i], topTube.getWidth() - 150, topTube.getHeight());
				bottomTubeRectangles[i] = new Rectangle(tubeX[i] + 100, Gdx.graphics.getHeight() / 2 - gap / 2 - bottomTube.getHeight() + tubeOffset[i], bottomTube.getWidth() - 150, bottomTube.getHeight());
			}

			if (birdY > 0) {

				velocity = velocity + gravity;
				birdY -= velocity;
			} else {

				gameState = 2;

			}

		} else if (gameState == 0) {
			if (Gdx.input.justTouched()) {
				gameState = 1;
			}
		} else if (gameState == 2){

			batch.draw(gameOver, Gdx.graphics.getWidth() / 2 - gameOver.getWidth() / 2, gameOverY);

			if (gameOverY > Gdx.graphics.getHeight() / 2 - gameOver.getHeight() / 2) {

				velocity = velocity + gravity;
				gameOverY -= velocity;

			} else {

				if (Gdx.input.justTouched()) {

					gameState = 0;
					startGame();
					score = 0;
					scoringTube = 0;
					velocity = 0;
					gameOverY = Gdx.graphics.getHeight();


				}

			}

		}

		if (flapState == 0) {
			flapState = 1;
		} else {
			flapState = 0;
		}

		font.draw(batch, String.valueOf(score), Gdx.graphics.getWidth() / 2 - 60, Gdx.graphics.getHeight() - 150);

		batch.end();

		birdCircle.set(Gdx.graphics.getWidth() / 2, birdY + birds[flapState].getHeight() / 3, birds[flapState].getHeight() / 4);



		//shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
		//shapeRenderer.setColor(Color.RED);
		//shapeRenderer.circle(birdCircle.x, birdCircle.y, birdCircle.radius);

		for (int i = 0; i < numberOfTubes; i++) {
			//shapeRenderer.rect(tubeX[i], Gdx.graphics.getHeight() / 2 + gap / 2 + tubeOffset[i], topTube.getWidth(), topTube.getHeight());
			//shapeRenderer.rect(tubeX[i], Gdx.graphics.getHeight() / 2 - gap / 2 - bottomTube.getHeight() + tubeOffset[i], bottomTube.getWidth(), bottomTube.getHeight());

			if (Intersector.overlaps(birdCircle, topTubeRectangles[i]) || Intersector.overlaps(birdCircle, bottomTubeRectangles[i])) {
				Gdx.app.log("Collision", "Yes!");
				gameState = 2;
			}
		}

		//shapeRenderer.end();

	}

}

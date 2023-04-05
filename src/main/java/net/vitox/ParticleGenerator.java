package net.vitox;

import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.minecraft.client.MinecraftClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Particle API This Api is free2use But u have to mention me.
 *
 * @author Vitox
 * @version 3.0
 */
public class ParticleGenerator {

  private final List<Particle> particles = new ArrayList<>();
  private final int amount;

  private int prevWidth;
  private int prevHeight;

  public ParticleGenerator(final int amount) {
    this.amount = amount;
  }

  public void draw(final int mouseX, final int mouseY) {
    if (MinecraftClient.getInstance() == null) return;
    if (particles.isEmpty()
        || prevWidth != MinecraftClient.getInstance().getWindow().getWidth()
        || prevHeight != MinecraftClient.getInstance().getWindow().getHeight()) {
      particles.clear();
      create();
    }

    prevWidth = MinecraftClient.getInstance().getWindow().getWidth();
    prevHeight = MinecraftClient.getInstance().getWindow().getHeight();

    for (final Particle particle : particles) {
      particle.fall();
      particle.interpolation();

      int range = 50;
      final boolean mouseOver =
          (mouseX >= particle.x - range)
              && (mouseY >= particle.y - range)
              && (mouseX <= particle.x + range)
              && (mouseY <= particle.y + range);

      if (mouseOver) {
        particles.stream()
            .filter(
                part ->
                    (part.getX() > particle.getX()
                            && part.getX() - particle.getX() < range
                            && particle.getX() - part.getX() < range)
                        && (part.getY() > particle.getY() && part.getY() - particle.getY() < range
                            || particle.getY() > part.getY()
                                && particle.getY() - part.getY() < range))
            .forEach(connectable -> particle.connect(connectable.getX(), connectable.getY()));
      }

      RenderUtils.drawCircle(particle.getX(), particle.getY(), particle.size, 0xffFFFFFF);
    }
  }

  private void create() {
    final Random random = new Random();

    for (int i = 0; i < amount; i++)
      particles.add(
          new Particle(
              random.nextInt(MinecraftClient.getInstance().getWindow().getWidth()),
              random.nextInt(MinecraftClient.getInstance().getWindow().getHeight())));
  }
}

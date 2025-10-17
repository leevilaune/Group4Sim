package simu.model;

import simu.framework.IEventType;

/**
 * Defines the various event types used in the simulation model.
 * <p>
 * Each event type represents a distinct phase or activity within the simulation process,
 * allowing the simulation framework to identify and handle events accordingly.
 * </p>
 * <p>
 * The possible event types include:
 * <ul>
 *   <li>{@code ARR1} - Arrival event</li>
 *   <li>{@code PLANNING} - Planning phase</li>
 *   <li>{@code IMPLEMENTATION} - Implementation phase</li>
 *   <li>{@code TESTING} - Testing phase</li>
 *   <li>{@code REVIEW} - Review phase</li>
 *   <li>{@code PRESENTATION} - Presentation phase</li>
 * </ul>
 * </p>
 */
public enum EventType implements IEventType {
	ARR1, PLANNING, IMPLEMENTATION, TESTING, REVIEW, PRESENTATION;
}

#version 450 core

in vec3 coords;

out vec4 pixelColor;

uniform samplerCube dayTexture;
uniform samplerCube nightTexture;
uniform float blendFactor;
uniform vec3 skyColor;

const float lowerLimit = 0.0;
const float upperLimit = 30.0;

void main(void) {
	if(coords.y > lowerLimit) {
		vec4 day = texture(dayTexture, coords);
		vec4 night = texture(nightTexture, coords);
		pixelColor = mix(day, night, blendFactor);
		if(coords.y < upperLimit) {
			float factor = (coords.y - lowerLimit) / (upperLimit - lowerLimit);
			pixelColor = mix(vec4(skyColor, 1), pixelColor, factor);
		}
	}
	else pixelColor = vec4(skyColor, 1);
}
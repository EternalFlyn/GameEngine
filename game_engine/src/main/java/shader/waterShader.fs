#version 450 core

in vec2 coords;
in vec3 toCameraVector;
in vec3 formLightVector;
in vec4 clipSpace;

out vec4 pixelColor;

uniform float waveStrength;
uniform float moveFactor;
uniform vec2 viewPlaneDistance;
uniform vec3 lightColor;
uniform vec3 skyColor;
uniform sampler2D reflectionTexture;
uniform sampler2D refractionTexture;
uniform sampler2D dudvMap;
uniform sampler2D normalMap;
uniform sampler2D depthMap;

const float shineDamper = 20.0;
const float reflectivity = 0.6;

void main(void) {

	vec2 distortionCoords = texture(dudvMap, vec2(coords.x + moveFactor, coords.y)).rg * 0.1;
	distortionCoords = distortionCoords + vec2(distortionCoords.x, distortionCoords.y + moveFactor);
	vec2 distortion = texture(dudvMap, distortionCoords).rg * 2 - 1;
	distortion *= waveStrength;
	
	vec2 normalizeDeviceSpace = (clipSpace.xy / clipSpace.w) / 2 + 0.5;
	
	float depth = texture(depthMap, normalizeDeviceSpace).r;
	float near = viewPlaneDistance.x;
	float far = viewPlaneDistance.y;
	float floorDistance = 2 * near * far / (far + near - (2 * depth - 1) * (far - near));
	
	depth = gl_FragCoord.z;
	float waterDistance = 2 * near * far / (far + near - (2 * depth - 1) * (far - near));
	
	float waterDepthFactor = clamp((floorDistance - waterDistance) / 0.5, 0, 1);
	
	vec2 waterCoords = clamp(normalizeDeviceSpace + distortion * waterDepthFactor, 0.001, 0.999);
	vec4 reflectColor = texture(reflectionTexture, waterCoords);
	vec4 refractColor = texture(refractionTexture, waterCoords);
	
	vec4 normalMap = texture(normalMap, distortionCoords);
	vec3 normal = vec3(normalMap.r * 2 - 1, normalMap.b * 3, normalMap.g * 2 - 1);
	normal = normalize(normal);
	
	vec3 viewVector = normalize(toCameraVector);
	float refractiveFactor = clamp(abs(dot(viewVector, normal)), 0, 1);
	refractiveFactor = pow(refractiveFactor, 3);
	
	vec3 reflectedLightDirection = reflect(normalize(formLightVector), normal);
	float specularFactor = max(dot(viewVector, reflectedLightDirection), 0);
	float dampedFactor = pow(specularFactor, shineDamper);
	vec3 specular = (dampedFactor * reflectivity) * lightColor * waterDepthFactor;

	pixelColor = mix(reflectColor, refractColor, refractiveFactor) + vec4(specular, 1);
	pixelColor = mix(pixelColor, vec4(skyColor, 1), 0.2);
	pixelColor.a = waterDepthFactor;
}
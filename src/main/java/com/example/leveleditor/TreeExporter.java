package com.example.leveleditor;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class TreeExporter {

    private final List<String> objects = new ArrayList<>(); // Lista para almacenar todos los objetos

    // Método para añadir un objeto al exportador
    public void addObject(String type, double x, double z) {
        // Transformación de coordenadas (ajusta según sea necesario)
        double godotX = x;
        double godotY = 2.7; // Altura fija
        double godotZ = z;

        // Determinar el ID del recurso externo según el tipo de objeto
        String resourceId;
        switch (type) {
            case "tree_1":
                resourceId = "5_rayau"; // Árbol 1
                break;
            case "tree_2":
                resourceId = "6_tree2"; // Árbol 2
                break;
            case "tree_3":
                resourceId = "7_tree3"; // Árbol 3
                break;
            case "rock_1":
                resourceId = "8_rock1"; // Roca 1
                break;
            case "rock_2":
                resourceId = "9_rock2"; // Roca 2
                break;
            case "rock_3":
                resourceId = "10_rock3"; // Roca 3
                break;
            case "misc_1":
                resourceId = "11_misc1"; // Misc 1
                break;
            case "misc_2":
                resourceId = "12_misc2"; // Misc 2
                break;
            case "misc_3":
                resourceId = "13_misc3"; // Misc 3
                break;
            default:
                throw new IllegalArgumentException("Tipo de objeto no válido: " + type);
        }

        // Crear el nodo del objeto
        String objectNode = String.format(Locale.US,
                "[node name=\"%s%d\" parent=\".\" instance=ExtResource(\"%s\")]\n" +
                        "transform = Transform3D(-3.3412e-08, -0.382189, 0.661971, 0, 0.661971, 0.382189, -0.764378, 1.6706e-08, -2.89357e-08, %.2f, %.2f, %.2f)\n\n",
                type, objects.size() + 1, resourceId, godotX, godotY, godotZ
        );
        objects.add(objectNode);
    }

    public void clearObjects() {
        objects.clear();
    }


    // Método para exportar a un archivo .tscn
    public void exportToFile(String filePath) throws IOException {
        try (FileWriter writer = new FileWriter(filePath)) {
            // Escribir la cabecera del archivo
            writer.write("[gd_scene load_steps=14 format=3 uid=\"uid://iq7h4wni125u\"]\n\n");

            // Escribir los recursos externos
            writer.write("[ext_resource type=\"Script\" path=\"res://Scripts/RoomDefScript.gd\" id=\"1_mhvj4\"]\n");
            writer.write("[ext_resource type=\"Texture2D\" uid=\"uid://f8sjn4yqti7n\" path=\"res://Sprites/floor2.png\" id=\"2_crq44\"]\n");
            writer.write("[ext_resource type=\"PackedScene\" uid=\"uid://yemt84rb13g0\" path=\"res://Scenes/player.tscn\" id=\"3_is1ow\"]\n");
            writer.write("[ext_resource type=\"PackedScene\" uid=\"uid://h1p4uoq5vhsg\" path=\"res://Scenes/camera.tscn\" id=\"4_hte8x\"]\n");
            writer.write("[ext_resource type=\"PackedScene\" uid=\"uid://bnt6qt7180qq0\" path=\"res://Scenes/Enviroment/tree_1.tscn\" id=\"5_rayau\"]\n"); // Árbol 1
            writer.write("[ext_resource type=\"PackedScene\" uid=\"uid://bnt6qt7180qq1\" path=\"res://Scenes/Enviroment/tree_2.tscn\" id=\"6_tree2\"]\n"); // Árbol 2
            writer.write("[ext_resource type=\"PackedScene\" uid=\"uid://bnt6qt7180qq2\" path=\"res://Scenes/Enviroment/tree_3.tscn\" id=\"7_tree3\"]\n"); // Árbol 3
            writer.write("[ext_resource type=\"PackedScene\" uid=\"uid://bnt6qt7180qq3\" path=\"res://Scenes/Enviroment/rock_1.tscn\" id=\"8_rock1\"]\n"); // Roca 1
            writer.write("[ext_resource type=\"PackedScene\" uid=\"uid://bnt6qt7180qq4\" path=\"res://Scenes/Enviroment/rock_2.tscn\" id=\"9_rock2\"]\n"); // Roca 2
            writer.write("[ext_resource type=\"PackedScene\" uid=\"uid://bnt6qt7180qq5\" path=\"res://Scenes/Enviroment/rock_3.tscn\" id=\"10_rock3\"]\n"); // Roca 3
            writer.write("[ext_resource type=\"PackedScene\" uid=\"uid://bnt6qt7180qq6\" path=\"res://Scenes/Enviroment/misc_1.tscn\" id=\"11_misc1\"]\n"); // Misc 1
            writer.write("[ext_resource type=\"PackedScene\" uid=\"uid://bnt6qt7180qq7\" path=\"res://Scenes/Enviroment/misc_2.tscn\" id=\"12_misc2\"]\n"); // Misc 2
            writer.write("[ext_resource type=\"PackedScene\" uid=\"uid://bnt6qt7180qq8\" path=\"res://Scenes/Enviroment/misc_3.tscn\" id=\"13_misc3\"]\n\n"); // Misc 3

            // Escribir los sub-recursos
            writer.write("[sub_resource type=\"StandardMaterial3D\" id=\"StandardMaterial3D_lskdr\"]\n");
            writer.write("diffuse_mode = 3\n");
            writer.write("specular_mode = 1\n");
            writer.write("albedo_texture = ExtResource(\"2_crq44\")\n\n");

            writer.write("[sub_resource type=\"PlaneMesh\" id=\"PlaneMesh_p6tl6\"]\n\n");

            writer.write("[sub_resource type=\"ConcavePolygonShape3D\" id=\"ConcavePolygonShape3D_mk0ac\"]\n");
            writer.write("data = PackedVector3Array(-0.5, 0.5, 0.5, 0.5, 0.5, 0.5, -0.5, -0.5, 0.5, 0.5, 0.5, 0.5, 0.5, -0.5, 0.5, -0.5, -0.5, 0.5, 0.5, 0.5, -0.5, -0.5, 0.5, -0.5, 0.5, -0.5, -0.5, -0.5, 0.5, -0.5, -0.5, -0.5, -0.5, 0.5, -0.5, -0.5, 0.5, 0.5, 0.5, 0.5, 0.5, -0.5, 0.5, -0.5, 0.5, 0.5, 0.5, -0.5, 0.5, -0.5, -0.5, 0.5, -0.5, 0.5, -0.5, 0.5, -0.5, -0.5, 0.5, 0.5, -0.5, -0.5, -0.5, -0.5, 0.5, 0.5, -0.5, -0.5, 0.5, -0.5, -0.5, -0.5, 0.5, 0.5, 0.5, -0.5, 0.5, 0.5, 0.5, 0.5, -0.5, -0.5, 0.5, 0.5, -0.5, 0.5, -0.5, 0.5, 0.5, -0.5, -0.5, -0.5, 0.5, 0.5, -0.5, 0.5, -0.5, -0.5, -0.5, 0.5, -0.5, 0.5, 0.5, -0.5, -0.5, -0.5, -0.5, -0.5)\n\n");

            writer.write("[sub_resource type=\"Environment\" id=\"Environment_ywln1\"]\n");
            writer.write("background_mode = 1\n\n");

            writer.write("[sub_resource type=\"BoxShape3D\" id=\"BoxShape3D_wjxqr\"]\n");
            writer.write("size = Vector3(1, 4, 10)\n\n");

            // Escribir los nodos principales
            writer.write("[node name=\"InitialRoom\" type=\"Node3D\"]\n");
            writer.write("script = ExtResource(\"1_mhvj4\")\n\n");

            writer.write("[node name=\"Floor\" type=\"MeshInstance3D\" parent=\".\"]\n");
            writer.write("transform = Transform3D(-3.74722e-07, 0, 4.85461, 0, 0.514113, 0, -8.57265, 0, -2.12202e-07, 0.0226865, -0.035954, 0)\n");
            writer.write("material_override = SubResource(\"StandardMaterial3D_lskdr\")\n");
            writer.write("mesh = SubResource(\"PlaneMesh_p6tl6\")\n\n");

            writer.write("[node name=\"StaticBody3D\" type=\"StaticBody3D\" parent=\"Floor\"]\n\n");

            writer.write("[node name=\"CollisionShape3D\" type=\"CollisionShape3D\" parent=\"Floor/StaticBody3D\"]\n");
            writer.write("transform = Transform3D(21.3409, 0, -1.13687e-13, 0, 8.78843, 0, 6.82121e-13, 0, 35.7898, 0, -0.951997, 0)\n");
            writer.write("shape = SubResource(\"ConcavePolygonShape3D_mk0ac\")\n\n");

            writer.write("[node name=\"player\" parent=\".\" instance=ExtResource(\"3_is1ow\")]\n");
            writer.write("process_priority = 75\n");
            writer.write("transform = Transform3D(-0.724015, 0, 123.828, 0, 123.83, 0, -123.828, 0, -0.724015, 2.638, 0.014, -0.205)\n\n");

            writer.write("[node name=\"WorldEnvironment\" type=\"WorldEnvironment\" parent=\".\"]\n");
            writer.write("environment = SubResource(\"Environment_ywln1\")\n\n");

            writer.write("[node name=\"DirectionalLight3D\" type=\"DirectionalLight3D\" parent=\"WorldEnvironment\"]\n");
            writer.write("transform = Transform3D(-0.224144, -0.5, 0.836516, -0.12941, 0.866025, 0.482963, -0.965926, 0, -0.258819, 13, 16, 0)\n");
            writer.write("light_energy = 0.808\n");
            writer.write("directional_shadow_mode = 0\n");
            writer.write("directional_shadow_split_1 = 0.276\n");
            writer.write("directional_shadow_max_distance = 784.3\n");
            writer.write("directional_shadow_pancake_size = 9.1\n\n");

            writer.write("[node name=\"Camera\" parent=\".\" instance=ExtResource(\"4_hte8x\")]\n");
            writer.write("transform = Transform3D(-4.37114e-08, -0.707107, 0.707107, 0, 0.707107, 0.707107, -1, 3.09086e-08, -3.09086e-08, 7, 8, 0)\n\n");

            writer.write("[node name=\"BoundsColliders\" type=\"Node3D\" parent=\".\"]\n\n");

            writer.write("[node name=\"StaticBody3D\" type=\"StaticBody3D\" parent=\"BoundsColliders\"]\n");
            writer.write("transform = Transform3D(5.91273, 0, 0, 0, 5.91273, 0, 0, 0, 5.91273, 0, 0, 9)\n");
            writer.write("collision_mask = 2\n\n");

            // Escribir los nodos de colisión
            String[] collisionNodes = {
                    "top1", "topbtwn", "top2", "right1", "rightbtwn", "right2", "left1", "leftbtwn", "left2", "bot1", "botbtwn", "bot2"
            };
            String[] transforms = {
                    "0.169127, 0, 0, 0, 0.169127, 0, 0, 0, 0.111658, -0.872897, 0.163282, -0.617463",
                    "0.169127, 0, 0, 0, 0.169127, 0, 0, 0, 0.0611771, -0.872898, 0.163282, -1.50271",
                    "0.169127, 0, 0, 0, 0.169127, 0, 0, 0, 0.111658, -0.873, 0.163, -2.399",
                    "-7.39277e-09, 0, -0.111659, 0, 0.169127, 0, 0.169127, 0, -4.88075e-09, -0.839889, 0.163282, -3.03449",
                    "-7.39277e-09, 0, -0.0611772, 0, 0.169127, 0, 0.169127, 0, -2.67414e-09, 0.0453556, 0.163282, -3.0345",
                    "-7.39277e-09, 0, -0.111659, 0, 0.169127, 0, 0.169127, 0, -4.88075e-09, 0.94165, 0.163, -3.0346",
                    "-7.39276e-09, 0, 0.111659, 0, 0.169127, 0, -0.169127, 0, -4.88075e-09, 0.937967, 0.163282, 0.0157124",
                    "-7.39276e-09, 0, 0.0611772, 0, 0.169127, 0, -0.169127, 0, -2.67414e-09, 0.0527225, 0.163282, 0.0157136",
                    "-7.39276e-09, 0, 0.111659, 0, 0.169127, 0, -0.169127, 0, -4.88075e-09, -0.843572, 0.163, 0.0158134",
                    "0.169127, 0, 0, 0, 0.169127, 0, 0, 0, 0.111659, 0.895318, 0.163282, -0.617463",
                    "0.169127, 0, 0, 0, 0.169127, 0, 0, 0, 0.0659768, 0.895, 0.163, -1.503",
                    "0.169127, 0, 0, 0, 0.169127, 0, 0, 0, 0.111659, 0.895, 0.163, -2.399"
            };

            for (int i = 0; i < collisionNodes.length; i++) {
                writer.write(String.format("[node name=\"%s\" type=\"CollisionShape3D\" parent=\"BoundsColliders/StaticBody3D\"]\n", collisionNodes[i]));
                writer.write(String.format("transform = Transform3D(%s)\n", transforms[i]));
                writer.write("shape = SubResource(\"BoxShape3D_wjxqr\")\n\n");
            }

            writer.write("[node name=\"Vegetation\" type=\"Node3D\" parent=\".\"]\n\n");

            // Escribir los nodos de los objetos colocados
            objects.stream().forEach(object -> {
                try {
                    writer.write(object);
                } catch (IOException e) {
                    throw new RuntimeException("Error al escribir el archivo", e);
                }
            });


            writer.write("[node name=\"Marker3D\" type=\"Marker3D\" parent=\".\"]\n");
            writer.write("transform = Transform3D(1, 0, 0, 0, 1, 0, 0, 0, 1, 4.68025, 0, -8.35926)\n");
        }
    }
}
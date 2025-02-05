package com.example.leveleditor;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TreeExporter {

    private final List<String> trees = new ArrayList<>();

    // Añadir un árbol con sus coordenadas en Godot
    public void addTree(double x, double z) {
        String treeNode = String.format(
                "[node name=\"Tree%d\" type=\"MeshInstance3D\" parent=\"Vegetation\"]\n" +
                        "transform = Transform3D(1, 0, 0, 0, 1, 0, 0, 0, 1, %.2f, 0, %.2f)\n" +
                        "mesh = SubResource(\"BoxMesh_dle13\")\n",
                trees.size() + 1, x, z
        );
        trees.add(treeNode);
    }

    // Exportar el contenido a un archivo .tscn
    public void exportToFile(String filePath) throws IOException {
        try (FileWriter writer = new FileWriter(filePath)) {
            // Encabezado de la escena
            writer.write("[gd_scene load_steps=4 format=3 uid=\"uid://iq7h4wni125u\"]\n\n");

            // Recursos externos (ajusta las rutas según tu proyecto)
            writer.write("[ext_resource type=\"Script\" path=\"res://Scripts/RoomDefScript.gd\" id=\"1_mhvj4\"]\n");
            writer.write("[ext_resource type=\"Texture2D\" uid=\"uid://f8sjn4yqti7n\" path=\"res://Sprites/floor2.png\" id=\"2_crq44\"]\n");
            writer.write("[ext_resource type=\"PackedScene\" uid=\"uid://yemt84rb13g0\" path=\"res://Scenes/player.tscn\" id=\"3_is1ow\"]\n");
            writer.write("[ext_resource type=\"PackedScene\" uid=\"uid://h1p4uoq5vhsg\" path=\"res://Scenes/camera.tscn\" id=\"4_hte8x\"]\n\n");

            // Sub-recursos (ajusta según tus necesidades)
            writer.write("[sub_resource type=\"StandardMaterial3D\" id=\"StandardMaterial3D_lskdr\"]\n");
            writer.write("albedo_texture = ExtResource(\"2_crq44\")\n\n");

            writer.write("[sub_resource type=\"BoxMesh\" id=\"BoxMesh_dle13\"]\n\n");

            // Nodo raíz
            writer.write("[node name=\"InitialRoom\" type=\"Node3D\"]\n");
            writer.write("script = ExtResource(\"1_mhvj4\")\n\n");

            // Nodo del suelo
            writer.write("[node name=\"Floor\" type=\"MeshInstance3D\" parent=\".\"]\n");
            writer.write("mesh = SubResource(\"PlaneMesh_p6tl6\")\n");
            writer.write("material_override = SubResource(\"StandardMaterial3D_lskdr\")\n\n");

            // Nodo padre para la vegetación
            writer.write("[node name=\"Vegetation\" type=\"Node3D\" parent=\".\"]\n\n");

            // Nodos de los árboles
            for (String tree : trees) {
                writer.write(tree);
            }

            // Nodo del jugador
            writer.write("[node name=\"player\" parent=\".\" instance=ExtResource(\"3_is1ow\")]\n\n");

            // Nodo de la cámara
            writer.write("[node name=\"Camera\" parent=\".\" instance=ExtResource(\"4_hte8x\")]\n");
        }
    }
}
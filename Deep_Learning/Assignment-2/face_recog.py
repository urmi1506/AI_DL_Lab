import cv2
import numpy as np
import os
from tensorflow.keras.models import Sequential
from tensorflow.keras.layers import Conv2D, MaxPooling2D, Flatten, Dense, Dropout
from sklearn.model_selection import train_test_split
import matplotlib.pyplot as plt

# STEP 1: Capture Dataset from Camera

def capture_images(person_name, num_samples=50, save_dir="dataset"):
    os.makedirs(f"{save_dir}/{person_name}", exist_ok=True)
    cap = cv2.VideoCapture(0)  # 0 = default webcam
    count = 0
    
    while count < num_samples:
        ret, frame = cap.read()
        if not ret:
            break
        
        gray = cv2.cvtColor(frame, cv2.COLOR_BGR2GRAY)
        face_cascade = cv2.CascadeClassifier(cv2.data.haarcascades + "haarcascade_frontalface_default.xml")
        faces = face_cascade.detectMultiScale(gray, 1.3, 5)
        
        for (x, y, w, h) in faces:
            face = gray[y:y+h, x:x+w]
            face = cv2.resize(face, (100, 100))  # Resize to 100x100
            cv2.imwrite(f"{save_dir}/{person_name}/{count}.jpg", face)
            count += 1
            cv2.imshow("Capturing", face)
        
        if cv2.waitKey(1) & 0xFF == ord('q'):
            break
    
    cap.release()
    cv2.destroyAllWindows()
    print(f"✅ Captured {count} images for {person_name}")


# STEP 2: Load Dataset

def load_dataset(dataset_path="dataset"):
    X, y = [], []
    label_map = {}
    current_label = 0
    
    for person in os.listdir(dataset_path):
        person_path = os.path.join(dataset_path, person)
        if not os.path.isdir(person_path):
            continue
        
        label_map[current_label] = person
        for img_file in os.listdir(person_path):
            img = cv2.imread(os.path.join(person_path, img_file), cv2.IMREAD_GRAYSCALE)
            img = cv2.resize(img, (100, 100))
            X.append(img)
            y.append(current_label)
        
        current_label += 1
    
    X = np.array(X).reshape(-1, 100, 100, 1) / 255.0
    y = np.array(y)
    return X, y, label_map


# Build CNN Model

def build_model():
    model = Sequential([
        Conv2D(32, (3,3), activation='relu', input_shape=(100,100,1)),
        MaxPooling2D((2,2)),
        Conv2D(64, (3,3), activation='relu'),
        MaxPooling2D((2,2)),
        Flatten(),
        Dense(128, activation='relu'),
        Dropout(0.5),
        Dense(1, activation='sigmoid')  # Binary classification
    ])
    
    model.compile(optimizer="adam", loss="binary_crossentropy", metrics=["accuracy"])
    return model

if __name__ == "__main__":
    # Capture faces for two persons (binary classification)
    print("📸 Capturing dataset for Person 1...")
    capture_images("Person1", num_samples=50)
    
    print("📸 Capturing dataset for Person 2...")
    capture_images("Person2", num_samples=50)
    
    # Load dataset
    X, y, label_map = load_dataset()
    X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.2, random_state=42)
    
    # Train model
    model = build_model()
    history = model.fit(X_train, y_train, epochs=10, batch_size=8, validation_data=(X_test, y_test))
    
    # Evaluate
    loss, acc = model.evaluate(X_test, y_test)
    print(f"✅ Test Accuracy: {acc*100:.2f}%")
    
    # Plot training history
    plt.plot(history.history['accuracy'], label='train_acc')
    plt.plot(history.history['val_accuracy'], label='val_acc')
    plt.legend()
    plt.xlabel("Epoch")
    plt.ylabel("Accuracy")
    plt.title("Training vs Validation Accuracy")
    plt.show()
    
    # Real-Time Prediction
    cap = cv2.VideoCapture(0)
    while True:
        ret, frame = cap.read()
        gray = cv2.cvtColor(frame, cv2.COLOR_BGR2GRAY)
        faces = cv2.CascadeClassifier(cv2.data.haarcascades + "haarcascade_frontalface_default.xml").detectMultiScale(gray, 1.3, 5)
        
        for (x, y0, w, h) in faces:
            face = gray[y0:y0+h, x:x+w]
            face = cv2.resize(face, (100, 100)) / 255.0
            face = face.reshape(1, 100, 100, 1)
            
            pred = model.predict(face)[0][0]
            label = label_map[1] if pred > 0.5 else label_map[0]
            
            cv2.rectangle(frame, (x,y0), (x+w,y0+h), (255,0,0), 2)
            cv2.putText(frame, label, (x, y0-10), cv2.FONT_HERSHEY_SIMPLEX, 0.9, (36,255,12), 2)
        
        cv2.imshow("Real-Time Face Recognition", frame)
        if cv2.waitKey(1) & 0xFF == ord('q'):
            break
    
    cap.release()
    cv2.destroyAllWindows()

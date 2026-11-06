package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [
        ChecklistCategoryEntity::class,
        ChecklistItemEntity::class,
        ReferenceCatalogEntity::class,
        InspectionEntity::class,
        InspectionResponseEntity::class,
        EmployeeInterviewEntity::class,
        InspectionSummaryEntity::class,
        InspectionRiskEntity::class,
        RiskEvidenceEntity::class,
        InspectionApprovalEntity::class,
        SyncQueueEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class HseDatabase : RoomDatabase() {
    abstract fun hseDao(): HseDao

    companion object {
        @Volatile
        private var INSTANCE: HseDatabase? = null

        fun getInstance(context: Context): HseDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    HseDatabase::class.java,
                    "adc_hse_inspections.db"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}

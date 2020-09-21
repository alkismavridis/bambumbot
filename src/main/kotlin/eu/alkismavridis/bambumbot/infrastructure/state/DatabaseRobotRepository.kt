package eu.alkismavridis.bambumbot.infrastructure.state

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import eu.alkismavridis.bambumbot.entities.BambumRobot
import eu.alkismavridis.bambumbot.entities.RobotRepository
import eu.alkismavridis.bambumbot.entities.brain_structure.BrainMetadata
import eu.alkismavridis.bambumbot.entities.brain_structure.BrainConfig
import eu.alkismavridis.bambumbot.entities.data_provider.DataProviderConfig
import eu.alkismavridis.bambumbot.entities.train.TrainerConfig
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Component
import java.util.*
import javax.persistence.*


@Entity
@Table(name="ROBOT")
class BambumRobotJpaEntity {
    @Id
    @Column(name = "ID")
    var id: UUID? = null

    @Column(name = "NAME")
    var name: String = ""

    @Column(name = "BRAIN_CONFIG")
    var brainConfig: String = ""

    @Column(name = "BRAIN_METADATA")
    var brainMetadata: String = ""

    @Column(name = "DATA_PROVIDER_CONFIG")
    var dataProviderConfig: String = ""

    @Column(name = "TRAINER_CONFIG")
    var trainerConfig: String = ""

    @Column(name = "BEST_STATE")
    var bestState: String = ""

    @Column(name = "CURRENT_STATE")
    var currentState: String = ""

    @Column(name = "LEAST_ERROR")
    var leastError = Double.MAX_VALUE
}

@Component
interface JpaRobotRepository : JpaRepository<BambumRobotJpaEntity, UUID> {}

@Component
class DatabaseRobotRepository(private val jpaRepo: JpaRobotRepository) : RobotRepository {
    private val mapper = jacksonObjectMapper()


    override fun getAll(): List<BambumRobot> {
        return this.jpaRepo.findAll().map(this::toCoreEntity)
    }

    override fun get(id: UUID): BambumRobot? {
        val robotData = this.jpaRepo.findById(id).orElse(null)
            ?: return null

        return this.toCoreEntity(robotData)
    }

    override fun save(robot: BambumRobot) : Unit {
        val jpaEntity = this.toJpaEntity(robot)
        this.jpaRepo.save(jpaEntity)
    }

    override fun delete(id: UUID) {
        this.jpaRepo.deleteById(id)
    }


    /// JPA - CORE MAPPINGS
    private fun toCoreEntity(jpaEntity: BambumRobotJpaEntity) : BambumRobot {
        return BambumRobot(
            jpaEntity.id!!,
            jpaEntity.name,
            this.mapper.readValue(jpaEntity.brainConfig, BrainConfig::class.java),
            this.mapper.readValue(jpaEntity.brainMetadata, BrainMetadata::class.java),
            this.mapper.readValue(jpaEntity.dataProviderConfig, DataProviderConfig::class.java),
            this.mapper.readValue(jpaEntity.trainerConfig, TrainerConfig::class.java),
            this.mapper.readValue(jpaEntity.bestState, DoubleArray::class.java),
            jpaEntity.leastError,
            this.mapper.readValue(jpaEntity.currentState, DoubleArray::class.java)
        )
    }

    private fun toJpaEntity(coreEntity: BambumRobot) : BambumRobotJpaEntity {
        val jpaEntity = BambumRobotJpaEntity()
        jpaEntity.id = coreEntity.id
        jpaEntity.name = coreEntity.name

        jpaEntity.brainConfig = this.mapper.writeValueAsString(coreEntity.brainConfig)
        jpaEntity.brainMetadata = this.mapper.writeValueAsString(coreEntity.brainMetadata)
        jpaEntity.dataProviderConfig = this.mapper.writeValueAsString(coreEntity.dataProviderConfig)
        jpaEntity.trainerConfig = this.mapper.writeValueAsString(coreEntity.trainerConfig)

        jpaEntity.bestState = this.mapper.writeValueAsString(coreEntity.bestState)
        jpaEntity.currentState = this.mapper.writeValueAsString(coreEntity.currentState)
        jpaEntity.leastError = coreEntity.leastError

        return jpaEntity
    }
}
